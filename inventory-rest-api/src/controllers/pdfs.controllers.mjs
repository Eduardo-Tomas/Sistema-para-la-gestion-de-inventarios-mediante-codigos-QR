import { pool } from "../database.mjs"
import { htmlToPDF } from "../functions/pdf.function.mjs"
import { formatDate, getElapsedDays } from "../functions/date.function.mjs"

export const getSpacesReport = async (req, res) => {
  // Dates are of the form yyyy-mm-dd
  const { startDate, endDate, selectedSpaces } = req.body

  let query = ""
  let args = 0

  if (startDate !== "" && endDate !== "") {
    query = "select distinct captureDate from Detection where captureDate between ? and ?"
    args = 2

  } else if (startDate !== "" && endDate === "") {
    query = "select distinct captureDate from Detection where captureDate = ?"
    args = 1

  } else {
    query = "select distinct captureDate from Detection"
  }

  const html = '<!DOCTYPE html><html lang="es"><head> <meta charset="utf-8"> <meta name="viewport" content="width=device-width, initial-scale=1.0"> <title>Reporte de espacios</title> <style> body { font-family: sans-serif; margin: 0; } .title { color: #2c2f3e; margin-top: 0; font-size: 1.5rem; text-align: center; } .date-info { color: #2c2f3e; border-radius: 0.5rem; margin: 1rem 5rem; padding: 1rem; border: 2px solid #3275f6; } .date-info > .space-info:last-child { border-bottom: none; } .date { color: #3275f6; font-size: 1.2rem; margin-top: 0; } .space-info { padding-bottom: 0.5rem; margin-bottom: 0.5rem; border-bottom: 1px solid #3275f6; } .space { font-size: 1rem; margin-top: 0; margin-bottom: 0.3rem; } .text { margin-top: 0; margin-bottom: 0; padding-left: 0.5rem; color: #999da4; } .text > span { color: #3275f6; } </style></head><body> <h1 class="title">Reporte de espacios</h1>'

  let section = ""

  try {
    let dates

    if (args === 0) {
      const [rows] = await pool.query(query)
      dates = rows

    } else if (args === 1) {
      const [rows] = await pool.query(query, [startDate])
      dates = rows

    } else {
      const [rows] = await pool.query(query, [startDate, endDate])
      dates = rows
    }

    dates = dates.map((date) => {
      const dateString = JSON.stringify(date.captureDate).replaceAll("\"", "")

      return dateString.slice(0, dateString.indexOf("T"))
    })

    let [objectIDs] = await pool.query("select objectID from Object")
    objectIDs = objectIDs.map((o) => {
      return o.objectID
    })

    let spaces 
    if (selectedSpaces.length > 0) {
      const [sE] = await pool.query("select spaceID, spaceName from space where spaceID in (?)", [selectedSpaces])

      spaces = sE

    } else {
      const [sE] = await pool.query("select spaceID, spaceName from Space")

      spaces = sE
    }

    //const [spaces] = await pool.query("select spaceID, spaceName from Space")
    const spaceNames = spaces.map(s => s.spaceName)

    const spaceIDs = spaces.map((s) => {
      return s.spaceID
    }) 

  
    for (let i = 0; i < dates.length; i++) {
      const date = dates[i]
      
      section = `${section}<section class="date-info"><h2 class="date">${formatDate(date)}</h2>`

      for (let j = 0; j < spaceIDs.length; j++) {
        const space = spaceIDs[j]

        let [foundObjects] = await pool.query("select objectID from Detection where captureDate = ? and spaceID = ?", [date, space])
        foundObjects = foundObjects.map((o) => {
          return o.objectID
        })

        const missingObjects = objectIDs.filter(x => !foundObjects.includes(x))

        section = `${section}<article class="space-info"> <h3 class="space">${spaceNames[j]}</h3> <p class="text"> Objetos faltantes: <span>${missingObjects.length}</span> </p> </article>`
      }
      section = `${section}</section>`
    }
  } catch (error) {
    console.log(error)

    return res.status(500).json({
      message: "Something went wrong"
    })
  }

  const pdf = await htmlToPDF(`${html}${section}</body></html>`)

  res.contentType("application/pdf")
  res.send(pdf)
}

export const getDetailedReport = async (req, res) => {
  // Dates are of the form yyyy-mm-dd
  const { startDate, endDate, selectedSpaces } = req.body

  let description = ""

  let query = ""
  let args = 0
  let firstCaptureDate, lastCaptureDate
  let totalDays = 0

  if (startDate !== "" && endDate !== "") {
    query = "select detectionID from Detection where captureDate between ? and ? and spaceID = ? and objectID = ?"
    args = 2

    description = `<p>Periodo de análisis comprendido del ${formatDate(startDate)} al ${formatDate(endDate)}.</p>`

    totalDays = getElapsedDays(startDate, endDate) + 1

  } else if (startDate !== "" && endDate === "") {
    query = "select detectionID from Detection where captureDate = ? and spaceID = ? and objectID = ?"
    args = 1

    description = `<p>Análisis del ${formatDate(startDate)}.</p>`

    totalDays = 1

  } else {
    query = "select detectionID from Detection where captureDate between ? and ? and spaceID = ? and objectID = ?"

    const [rows] = await pool.query("select min(captureDate) as firstCaptureDate from Detection")

    const [result] = await pool.query("select max(captureDate) as lastCaptureDate from Detection")

    const sDate = JSON.stringify(rows[0].firstCaptureDate).replaceAll("\"", "")
    firstCaptureDate = sDate.slice(0, sDate.indexOf("T"))

    const eDate = JSON.stringify(result[0].lastCaptureDate).replaceAll("\"", "")
    lastCaptureDate = eDate.slice(0, eDate.indexOf("T"))

    description = `<p>Periodo de análisis comprendido del ${formatDate(firstCaptureDate)} al ${formatDate(lastCaptureDate)}.</p>`

    totalDays = getElapsedDays(firstCaptureDate, lastCaptureDate) + 1
  }

  const html = '<!DOCTYPE html><html lang="es"><head> <meta charset="utf-8"> <meta name="viewport" content="width=device-width, initial-scale=1.0"> <title>Reporte detallado por espacio</title> <style> body { font-family: sans-serif; margin: 0; } .title { color: #2c2f3e; margin-top: 0; font-size: 1.5rem; text-align: center; } .title + p { margin-left: 5rem; font-size: 1.1rem; } .space-info { color: #2c2f3e; border-radius: 0.5rem; margin: 1rem 5rem; padding: 1rem; border: 2px solid #3275f6; } .space { color: #3275f6; font-size: 1.2rem; margin-top: 0; } .object-info { padding-bottom: 0.5rem; margin-bottom: 0.5rem; border-bottom: 1px solid #3275f6; } .space-info > .object-info:last-child { border-bottom: none; } .object { font-size: 1rem; margin-top: 0; margin-bottom: 0.3rem; } .text { margin-top: 0; margin-bottom: 0; padding-left: 0.5rem; color: #999da4; } .text > span { color: #3275f6; } </style></head><body> <h1 class="title">Reporte detallado por espacio</h1>'

  
  let section = ""

  try {
    let spaces
    if (selectedSpaces.length > 0) {
      const [sE] = await pool.query("select spaceID, spaceName from Space where spaceID in (?)", [selectedSpaces])

      spaces = sE

    } else {
      const [sE] = await pool.query("select spaceID, spaceName from Space")

      spaces = sE
    }
    
    //const [spaces] = await pool.query("select spaceID, spaceName from Space")
    const spaceNames = spaces.map(s => s.spaceName)

    const spaceIDs = spaces.map((s) => {
      return s.spaceID
    })

    const [objects] = await pool.query("select objectID, objectName from Object")
    const objectNames = objects.map(o => o.objectName)

    const objectIDs = objects.map((o) => {
      return o.objectID
    })

    for (let i = 0; i < spaceIDs.length; i++) {
      const space = spaceIDs[i];

      section = `${section}<section class="space-info"><h2 class="space">${spaceNames[i]}</h2>`

      for (let j = 0; j < objectIDs.length; j++) {
        const object = objectIDs[j];

        let detected = ""

        let detections
        if (args === 0) {
          const [rows] = await pool.query(query, [firstCaptureDate, lastCaptureDate, space, object])
          detections = rows

        } else if (args === 1) {
          const [rows] = await pool.query(query, [startDate, space, object])
          detections = rows

        } else {
          const [rows] = await pool.query(query, [startDate, endDate, space, object])
          detections = rows
        }

        if (detections.length <= 0) {
          // The object was not detected in that period of time
          detected = `0 de ${totalDays}`

        } else {
          // The object was detected in that period of time
          detected = `${detections.length} de ${totalDays}`
        }

        section = `${section}<article class="object-info"><h3 class="object">${objectNames[j]}</h3><p class="text">Detectado: <span>${detected}</span></p></article>`
      }
      section = `${section}</section>`
    }
  } catch (error) {
    return res.status(500).json({
      message: "Something went wrong"
    })
  }

  const pdf = await htmlToPDF(`${html}${description}${section}</body></html>`)

  res.contentType("application/pdf")
  res.send(pdf)
}
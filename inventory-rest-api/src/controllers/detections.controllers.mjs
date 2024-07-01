import { pool } from "../database.mjs"

// Insert one at a time
export const createDetection = async (req, res) => {
  const { objectID, spaceID, captureDate, captureTime } = req.body
  
  try {
    const [rows] = await pool.query("insert into Detection (detectionID, objectID, spaceID, captureDate, captureTime) values (null, ?, ?, ?, ?)", [objectID, spaceID, captureDate, captureTime])

    res.send({
      // insertId: rows.insertId, -orig
      detectionID: rows.insertId,
      objectID,
      spaceID,
      captureDate,
      captureTime
    })
  } catch (error) {
    console.log(error)
    return res.status(500).json({
      message: "Something went wrong"
    })
  }
}

export const getDetections = async (req, res) => {
  try {
    const [rows] = await pool.query("select d.detectionID, d.captureDate, d.captureTime, d.objectID, o.objectName, o.objectPrice, o.objectQR, d.spaceID, s.spaceName, s.spaceQR from(Object o inner join Detection d on o.objectID = d.objectID) inner join Space s on d.spaceID = s.spaceID")

    res.json(rows)
  } catch (error) {
    return res.status(500).json({
      message: "Something went wrong"
    })
  }
}

export const getDetectionsByLastDate = async (req, res) => {
  try {
    const [rows] = await pool.query("select d.detectionID, d.captureDate, d.captureTime, d.objectID, o.objectName, o.objectPrice, o.objectQR, d.spaceID, s.spaceName, s.spaceQR from(Object o inner join Detection d on o.objectID = d.objectID) inner join Space s on d.spaceID = s.spaceID where d.captureDate = (select max(captureDate) from Detection)")

    res.json(rows)
  } catch (error) {
    return res.status(500).json({
      message: "Something went wrong"
    })
  }
}

export const getDetectionsBySpaceAndLastDate = async (req, res) => {
  const id = req.params.id
  
  const queryString = "select d.detectionID, d.captureDate, d.captureTime, d.objectID, o.objectName, o.objectPrice, o.objectQR, d.spaceID, s.spaceName, s.spaceQR from(Object o inner join Detection d on o.objectID = d.objectID) inner join Space s on d.spaceID = s.spaceID where d.captureDate = (select max(captureDate) from Detection where spaceID = ?) and d.spaceID = ?"

  try {
    const [rows] = await pool.query(queryString, [id, id])

    res.json(rows)
  } catch (error) {
    return res.status(500).json({
      message: "Something went wrong"
    })
  }
}
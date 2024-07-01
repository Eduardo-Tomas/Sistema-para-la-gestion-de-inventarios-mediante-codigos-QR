import { pool } from "../database.mjs"
import { DateTime } from "luxon"

// Insert one at a time
export const createNotification = async (req, res) => {
  const { objectID, spaceID, notificationTypeID, limitAmount, isActive } = req.body

  try {
    const [rows] = await pool.query("insert into Notification (notificationID, objectID, spaceID, notificationTypeID, limitAmount, isActive) values (null, ?, ?, ?, ?, ?)", [objectID, spaceID, notificationTypeID, limitAmount, isActive])

    res.send({
      notificationID: rows.insertId,
      objectID,
      spaceID,
      notificationTypeID,
      limitAmount,
      isActive
    })
  } catch (error) {
    return res.status(500).json({
      message: "Something went wrong"
    })
  }
}

export const getNotifications = async (req, res) => {
  try {
    const query = "select n.notificationID, n.objectID, o.objectName, n.spaceID, s.spaceName, n.notificationTypeID, nt.description, n.limitAmount, n.isActive from((Object o inner join Notification n on o.objectID = n.objectID) inner join Space s on n.spaceID = s.spaceID) inner join NotificationType nt on n.notificationTypeID = nt.notificationTypeID"

    const [rows] = await pool.query(query)

    res.json(rows)
  } catch (error) {
    return res.status(500).json({
      message: "Something went wrong"
    })
  }
}

export const getNotification = async (req, res) => {
  const query = "select n.notificationID, n.objectID, o.objectName, n.spaceID, s.spaceName, n.notificationTypeID, nt.description, n.limitAmount, n.isActive from((Object o inner join Notification n on o.objectID = n.objectID) inner join Space s on n.spaceID = s.spaceID) inner join NotificationType nt on n.notificationTypeID = nt.notificationTypeID where notificationID = ?"

  try {
    const [rows] = await pool.query(query, [req.params.id])

    if (rows.length <= 0) return res.status(404).json({
      message: "Notification not found"
    })

    res.json(rows[0])
  } catch (error) {
    return res.status(500).json({
      message: "Something went wrong"
    })
  }
}

export const getStockLimit = async (req, res) => {
  // 30 days
  const period = 30

  const today = DateTime.now()/*.minus({ days: 15 })*/
  
  const startDate = today.minus({ days: period }).toFormat('yyyy-MM-dd')
  const endDate = today.minus({ days: 1 }).toFormat('yyyy-MM-dd')

  console.log("today", today.toFormat('yyyy-MM-dd'))
  console.log("startDate", startDate)
  console.log("endDate", endDate)

  let message = ""

  try {
    // Gets the notifications that are type stockLimit and that are active
    const [notifications] = await pool.query("select o.objectID, o.objectName, s.spaceID, s.spaceName, n.limitAmount from (Object o inner join Notification n on o.objectID = n.objectID) inner join Space s on n.spaceID = s.spaceID where notificationTypeID = 1 and isActive = 1")

    if (notifications.length > 0) {
      message = "En los últimos 30 días: \n"
    }

    for (let i = 0; i < notifications.length; i++) {
      const element = notifications[i]
      const objectID = element.objectID
      const objectName = element.objectName
      const spaceID = element.spaceID
      const spaceName = element.spaceName
      const limitAmount = element.limitAmount

      const [detections] = await pool.query("select detectionID from Detection where objectID = ? and spaceID = ? and captureDate between ? and ?", [objectID, spaceID, startDate, endDate])

      if (detections.length <= limitAmount) {
        console.log(`El objeto ${objectName} que se encuentra en el espacio ${spaceName} se encontró ${detections.length} veces y su cantidad límite es ${limitAmount}`)

        message = `${message}El objeto ${objectName} del espacio ${spaceName} se encontró ${detections.length} veces, (cantidad límite ${limitAmount}).\n`
      }
    }

    res.send({ message: message })
    
  } catch (error) {
    return res.status(500).json({
      message: "Something went wrong"
    })
  }
}

export const getAbsence = async (req, res) => {
  // 30 days
  const period = 7

  const today = DateTime.now()

  const startDate = today.minus({ days: period }).toFormat('yyyy-MM-dd')
  const endDate = today.minus({ days: 1 }).toFormat('yyyy-MM-dd')

  console.log("today", today.toFormat('yyyy-MM-dd'))
  console.log("startDate", startDate)
  console.log("endDate", endDate)

  let message = ""

  try {
    // Gets the notifications that are type absence and that are active
    const [notifications] = await pool.query("select o.objectID, o.objectName, s.spaceID, s.spaceName from (Object o inner join Notification n on o.objectID = n.objectID) inner join Space s on n.spaceID = s.spaceID where notificationTypeID = 2 and isActive = 1")

    if (notifications.length > 0) {
      message = "En los últimos 7 días.\nLos siguientes objetos no fueron encontrados:\n"
    }

    for (let i = 0; i < notifications.length; i++) {
      const element = notifications[i]
      const objectID = element.objectID
      const objectName = element.objectName
      const spaceID = element.spaceID
      const spaceName = element.spaceName

      const [detections] = await pool.query("select detectionID from Detection where objectID = ? and spaceID = ? and captureDate between ? and ?", [objectID, spaceID, startDate, endDate])

      if (detections.length === 0) {
        console.log(`El objeto ${objectName} del espacio ${spaceName} no fue encontrado`)

        message = `${message}El objeto ${objectName} del espacio ${spaceName}.\n`
      }
    }

    res.send({ message: message })

  } catch (error) {
    return res.status(500).json({
      message: "Something went wrong"
    })
  }
}

export const updateNotification = async (req, res) => {
  const { id } = req.params // Equivalent to const id = req.params.id

  const { objectID, spaceID, notificationTypeID, limitAmount, isActive } = req.body

  const query = "update Notification set objectID = ifnull(?, objectID), spaceID = ifnull(?, spaceID), notificationTypeID = ifnull(?, notificationTypeID), limitAmount = ifnull(?, limitAmount), isActive = ifnull(?, isActive) where notificationID = ?"

  const updatedQuery = "select * from Notification where notificationID = ?"

  try {
    const [result] = await pool.query(query, [objectID, spaceID, notificationTypeID, limitAmount, isActive, id])

    if (result.affectedRows === 0) return res.status(404).json({
      message: "Notification not found"
    })

    const [rows] = await pool.query(updatedQuery, [id])

    // Return an update
    res.json(rows[0])
  } catch (error) {
    return res.status(500).json({
      message: "Something went wrong"
    })
  }
}

export const deleteNotification = async (req, res) => {
  try {
    const [result] = await pool.query("delete from Notification where notificationID = ?", [req.params.id])

    if (result.affectedRows <= 0) return res.status(404).json({
      message: "Notification not found"
    })

    // Yes it could delete but it is not returning anything
    res.sendStatus(204)
  } catch (error) {
    return res.status(500).json({
      message: "Something went wrong"
    })
  }
}
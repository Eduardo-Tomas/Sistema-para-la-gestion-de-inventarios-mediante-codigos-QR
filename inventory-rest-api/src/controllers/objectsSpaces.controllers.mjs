import { pool } from "../database.mjs"

export const createOSsFromObject = async (req, res) => {
  const { objectID } = req.body
  
  const spacesQuery = "select spaceID from Space"

  try {
    const [spaceRows] = await pool.query(spacesQuery)

    for (let i = 0; i < spaceRows.length; i++) {
      await pool.query("insert into ObjectSpace (objectSpaceID, objectID, spaceID) values (null, ?, ?)", [objectID, spaceRows[i].spaceID])
    }

    // Yes it could insert but it is not returning anything
    res.sendStatus(204)
  } catch (error) {
    return res.status(500).json({
      message: "Something went wrong"
    })
  }
}

export const createOSsFromSpace = async (req, res) => {
  const { spaceID } = req.body

  const objectsQuery = "select objectID from Object"

  try {
    const [objectRows] = await pool.query(objectsQuery)

    for (let i = 0; i < objectRows.length; i++) {
      await pool.query("insert into ObjectSpace (objectSpaceID, objectID, spaceID) values (null, ?, ?)", [objectRows[i].objectID, spaceID])
    }

    // Yes it could insert but it is not returning anything
    res.sendStatus(204)
  } catch (error) {
    return res.status(500).json({
      message: "Something went wrong"
    })
  }
}

export const getObjectsSpaces = async (req, res) => {
  try {
    const [rows] = await pool.query("select * from ObjectSpace")

    res.json(rows)
  } catch (error) {
    return res.status(500).json({
      message: "Something went wrong"
    })
  }
}

export const getObjectsSpacesBySpace = async (req, res) => {
  try {
    const [rows] = await pool.query("select * from ObjectSpace where spaceID = ?", [req.params.id])

    res.json(rows)
  } catch (error) {
    return res.status(500).json({
      message: "Something went wrong"
    })
  }
}
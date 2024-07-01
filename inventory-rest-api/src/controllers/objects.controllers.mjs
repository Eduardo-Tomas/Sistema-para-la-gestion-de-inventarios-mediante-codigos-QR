import { pool } from "../database.mjs"
import QRCode from "qrcode"
import path from "node:path"
import { existsSync, unlinkSync } from "node:fs"
import { fileURLToPath } from "url"

// Insert one at a time
export const createObject = async (req, res) => {
  const { objectName, objectPrice } = req.body

  // Prefix 0 for object
  let objectQR = "0"

  const filename = fileURLToPath(import.meta.url);
  // dirname tells you the absolute path of the directory containing 
  // the currently executing file (similar to __dirname)
  const dirname = path.dirname(filename)

  try {
    const [rows] = await pool.query("insert into Object (objectID, objectName, objectPrice, objectQR) values (null, ?, ?, ?)", [objectName, objectPrice, objectQR])

    // We update the objectQR by adding the objectID at the end
    const insertedID = rows.insertId
    objectQR = `${objectQR}${insertedID}`
    await pool.query("update Object set objectQR = ? where objectID = ?", [objectQR, insertedID])

    const svgPath = path.resolve(dirname, `../../uploads/objects/${objectQR}.svg`)

    const opts = {
      type: "svg",
      // version: 1, // If no version is specified, the more suitable value will be used
      errorCorrectionLevel: "H"
    }

    QRCode.toFile(svgPath, objectQR, opts, function (err) {
      if (err) console.log(err)
      console.log('done')
    })

    res.send({
      // insertId: rows.insertId, -orig
      objectID: rows.insertId,
      objectName,
      objectPrice,
      objectQR
    })
  } catch (error) {
    return res.status(500).json({
      message: "Something went wrong"
    })
  }
}

export const getObjects = async (req, res) => {
  try {
    const [rows] = await pool.query("select * from Object")

    res.json(rows)
  } catch (error) {
    return res.status(500).json({
      message: "Something went wrong"
    })
  }
}

export const getObject = async (req, res) => {
  const queryString = "select * from Object where objectID = ?"

  try {  
    const [rows] = await pool.query(queryString, [req.params.id])

    if (rows.length <= 0) return res.status(404).json({
      message: "Object not found"
    })

    res.json(rows[0])
  } catch (error) {
    return res.status(500).json({
      message: "Something went wrong"
    })
  }
}

export const updateObject = async (req, res) => {
  const { id } = req.params // Equivalent to const id = req.params.id

  const { objectName, objectPrice } = req.body

  const queryString = "update Object set objectName = ifnull(?, objectName), objectPrice = ifnull(?, objectPrice) where objectID = ?"

  const updatedQuery = "select * from Object where objectID = ?"

  try {
    const [result] = await pool.query(queryString, [objectName, objectPrice, id])

    if (result.affectedRows === 0) return res.status(404).json({
      message: "Object not found"
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

export const deleteObject = async (req, res) => {
  const queryString = "delete from Object where objectID = ?"

  // Prefix 0 for object
  const code = `0${req.params.id}`

  try {
    const [result] = await pool.query(queryString, [req.params.id])

    if (result.affectedRows <= 0) return res.status(404).json({
      message: "Object not found"
    })

    const filename = fileURLToPath(import.meta.url);
    // dirname tells you the absolute path of the directory containing 
    // the currently executing file (similar to __dirname)
    const dirname = path.dirname(filename);

    const imagePath = path.resolve(dirname, `../../uploads/objects/${code}.svg`)

    if (existsSync(imagePath)) {
      unlinkSync(imagePath)
    }

    // Yes it could delete but it is not returning anything
    res.sendStatus(204)
  } catch (error) {
    console.log(error)
    return res.status(500).json({
      message: "Something went wrong"
    })
  }
}
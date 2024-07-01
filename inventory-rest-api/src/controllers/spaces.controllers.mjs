import { pool } from "../database.mjs"
import QRCode from "qrcode"
import path from "node:path"
import { existsSync, unlinkSync } from "node:fs"
import { fileURLToPath } from "url"

// Insert one at a time
export const createSpace = async (req, res) => {
  const { spaceName } = req.body

  // Prefix 1 for space
  let spaceQR = "1"

  const filename = fileURLToPath(import.meta.url);
  // dirname tells you the absolute path of the directory containing 
  // the currently executing file (similar to __dirname)
  const dirname = path.dirname(filename);

  try {
    const [rows] = await pool.query("insert into Space (spaceID, spaceName, spaceQR) values (null, ?, ?)", [spaceName, spaceQR])

    // We update the spaceQR by adding the spaceID at the end
    const insertedID = rows.insertId
    spaceQR = `${spaceQR}${insertedID}`
    await pool.query("update Space set spaceQR = ? where spaceID = ?", [spaceQR, insertedID])

    const svgPath = path.resolve(dirname, `../../uploads/spaces/${spaceQR}.svg`)

    const opts = {
      type: "svg",
      // version: 1, // If no version is specified, the more suitable value will be used
      errorCorrectionLevel: "H"
    }

    QRCode.toFile(svgPath, spaceQR, opts, function (err) {
      if (err) console.log(err)
      console.log('done')
    })

    res.send({
      // insertId: rows.insertId, -orig
      spaceID: rows.insertId,
      spaceName,
      spaceQR
    })
  } catch (error) {
    return res.status(500).json({
      message: "Something went wrong"
    })
  }
}

export const getSpaces = async (req, res) => {
  try {
    const [rows] = await pool.query("select * from Space")

    res.json(rows)
  } catch (error) {
    return res.status(500).json({
      message: "Something went wrong"
    })
  }
}

export const getSpace = async (req, res) => {
  const queryString = "select * from Space where spaceID = ?"

  try {
    const [rows] = await pool.query(queryString, [req.params.id])

    if (rows.length <= 0) return res.status(404).json({
      message: "Space not found"
    })

    res.json(rows[0])
  } catch (error) {
    return res.status(500).json({
      message: "Something went wrong"
    })
  }
}

export const updateSpace = async (req, res) => {
  const { id } = req.params // Equivalent to const id = req.params.id

  const { spaceName } = req.body

  const queryString = "update Space set spaceName = ifnull(?, spaceName) where spaceID = ?"
  const updatedQuery = "select * from Space where spaceID = ?"

  try {
    const [result] = await pool.query(queryString, [spaceName, id])

    if (result.affectedRows === 0) return res.status(404).json({
      message: "Space not found"
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

export const deleteSpace = async (req, res) => {
  // let queryString, querySpaceCode, code = ""
  const queryString = "delete from Space where spaceID = ?"

  // Prefix 1 for space
  const code = `1${req.params.id}`

  try {
    const [result] = await pool.query(queryString, [req.params.id])

    if (result.affectedRows <= 0) return res.status(404).json({
      message: "Space not found"
    })

    const filename = fileURLToPath(import.meta.url);
    // dirname tells you the absolute path of the directory containing 
    // the currently executing file (similar to __dirname)
    const dirname = path.dirname(filename);

    const imagePath = path.resolve(dirname, `../../uploads/spaces/${code}.svg`)

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
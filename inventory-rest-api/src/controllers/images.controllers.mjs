import path from "node:path"
import { existsSync } from "node:fs"
import { fileURLToPath } from "url"

export const getImage = (req, res) => {
  // Type object or space
  const type = req.params.type
  const image = req.params.image
  
  console.log(type)
  console.log(image)

  const filename = fileURLToPath(import.meta.url);
  // dirname tells you the absolute path of the directory containing 
  // the currently executing file (similar to __dirname)
  const dirname = path.dirname(filename);

  const imagePath = path.resolve(dirname, `../../uploads/${type}/${image}`)
  console.log(`imagePath: ${imagePath}`)

  if (existsSync(imagePath)) {
    res.sendFile(imagePath)
  } else {
    res.status(404).json({
      message: "Image not found"
    })
  }
}
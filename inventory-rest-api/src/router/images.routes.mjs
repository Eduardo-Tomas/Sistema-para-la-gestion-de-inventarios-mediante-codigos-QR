import { Router } from "express"
import { getImage } from "../controllers/images.controllers.mjs"

const router = Router()

// Read
router.get("/:type/:image", getImage)

export default router
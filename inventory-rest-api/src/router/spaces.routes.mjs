import { Router } from "express"
import { createSpace, getSpaces, getSpace, updateSpace, deleteSpace } from "../controllers/spaces.controllers.mjs"

const router = Router()

// Create
router.post("/spaces", createSpace)

// Read
router.get("/spaces", getSpaces)

router.get("/spaces/:id", getSpace)

// Update
// Update all data if we use put, if we want to make a partial update we use patch
router.patch("/spaces/:id", updateSpace)

// Delete
router.delete("/spaces/:id", deleteSpace)

export default router
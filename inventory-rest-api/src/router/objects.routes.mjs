import { Router } from "express"
import { createObject, getObjects, getObject, updateObject, deleteObject } from "../controllers/objects.controllers.mjs"

const router = Router()

// Create
router.post("/objects", createObject)

// Read
router.get("/objects", getObjects)

router.get("/objects/:id", getObject)

// Update
// Update all data if we use put, if we want to make a partial update we use patch
router.patch("/objects/:id", updateObject)

// Delete
router.delete("/objects/:id", deleteObject)

export default router
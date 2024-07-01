import { Router } from "express"
import { createOSsFromObject, createOSsFromSpace, getObjectsSpaces, getObjectsSpacesBySpace } from "../controllers/objectsSpaces.controllers.mjs"

const router = Router()

// Create
router.post("/objectsSpacesFO", createOSsFromObject)

router.post("/objectsSpacesFS", createOSsFromSpace)

// Read
router.get("/objectsSpaces", getObjectsSpaces)

router.get("/objectsSpaces/space/:id", getObjectsSpacesBySpace)

export default router
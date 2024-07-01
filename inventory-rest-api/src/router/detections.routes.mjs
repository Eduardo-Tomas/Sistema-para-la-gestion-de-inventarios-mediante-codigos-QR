import { Router } from "express"
import { createDetection, getDetections, getDetectionsByLastDate, getDetectionsBySpaceAndLastDate } from "../controllers/detections.controllers.mjs"


const router = Router()

// Create
router.post("/detections", createDetection)

// Read
router.get("/detections", getDetections)

router.get("/detections/last", getDetectionsByLastDate)

router.get("/detections/last/:id", getDetectionsBySpaceAndLastDate)

export default router
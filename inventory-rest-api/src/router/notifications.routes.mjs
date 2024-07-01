import { Router } from "express"
import { createNotification, getNotifications, getNotification, updateNotification, deleteNotification, getStockLimit, getAbsence } from "../controllers/notifications.controllers.mjs"

const router = Router()

// Create
router.post("/notifications", createNotification)

// Read
router.get("/notifications", getNotifications)

router.get("/notifications/stockLimit", getStockLimit)

router.get("/notifications/absence", getAbsence)

router.get("/notifications/:id", getNotification)

// Update
// Update all data if we use put, if we want to make a partial update we use patch
router.patch("/notifications/:id", updateNotification)

// Delete
router.delete("/notifications/:id", deleteNotification)

export default router
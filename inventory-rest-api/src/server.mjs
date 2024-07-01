import express from "express"
import objectsRoutes from "./router/objects.routes.mjs" // Since it is a default I can name it whatever I want

import spacesRoutes from "./router/spaces.routes.mjs"

import detectionsRoutes from "./router/detections.routes.mjs"

import imagesRoutes from "./router/images.routes.mjs"

import objectsSpacesRoutes from "./router/objectsSpaces.routes.mjs"

import reportsRoutes from "./router/pdfs.routes.mjs"

import notificationsRoutes from "./router/notifications.routes.mjs"

// The application is created with express
const app = express()

app.use(express.json())

const PORT = process.env.PORT ?? 3000

// Prefix route /api
app.use("/api", objectsRoutes)

app.use("/api", spacesRoutes)

app.use("/api", detectionsRoutes)

app.use("/api", objectsSpacesRoutes)

app.use("/api/images", imagesRoutes)

app.use("/api/reports", reportsRoutes)

app.use("/api", notificationsRoutes)

// The last one
app.use((req, res, next) => {
  res.status(404).json({
    message: "URL not found"
  })
})

// On which port our app will work
app.listen(PORT, () => {
  console.log(`server listening on port http://localhost:${PORT}`);
})
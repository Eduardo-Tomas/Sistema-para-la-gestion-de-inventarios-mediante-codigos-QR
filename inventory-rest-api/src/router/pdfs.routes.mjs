import { Router } from "express"
import { getSpacesReport, getDetailedReport } from "../controllers/pdfs.controllers.mjs"

const router = Router()

router.patch("/spacesReport.pdf", getSpacesReport)

router.patch("/detailedReportBySpace.pdf", getDetailedReport)

export default router

// startDate and endDate are Strings "yyyy-mm-dd"
export const getElapsedDays = (startDate, endDate) => {
  const d1 = new Date(startDate)
  const d2 = new Date(endDate)

  const elapsed = d2 - d1

  return elapsed / 86400000 // Number of milliseconds in a day
}


// date is a String of the form "yyyy-mm-dd"
export const formatDate = (date) => {
  const day = date.slice(8, 10)
  const month = date.slice(5, 7)
  const year = date.slice(0, 4)
  
  return `${day}-${month}-${year}`
}
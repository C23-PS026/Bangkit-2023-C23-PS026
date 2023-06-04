const express = require('express')
const cors = require('cors')
const fs = require('fs')
const app = express()

app.use(cors())
app.use(express.json())
app.use(express.urlencoded({extended: true}))

app.get('/', (req, res) => {
    res.json({
        message: 'MeatFYI API is now live'
    })
})

const meat = require('./routes/meat.js')
app.use('/meat', meat)

const articles = require('./routes/articles.js')
app.use('/articles', articles)

// Needs to be the last
app.get('*', (req, res) => {
    res.json({
        message: 'Invalid URL'
    })
})

const port = process.env.PORT || 3000
app.listen(port, () => {
    console.log(`App is running on port ${port}`)
})
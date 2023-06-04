const express = require('express')
const router = express.Router()

const articles = [
    {message: 'JSON article masih kosong'}
]
router.get('/', (req, res) => {
    res.json(articles)
})

module.exports = router
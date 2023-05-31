const express = require('express')
const router = express.Router()

const Users = []

router.get('/', (req, res) => {
    res.send('Halaman Sign In/Sign Up')
})

router.post('/', (req, res) => {
    if (!req.body.username || !req.body.password) {
        res.status('400')
        res.send('Username or password invalid!')
    } else {
        Users.filter((user) => {
            if (user.id === req.body.id) {
                res.json({message: 'Username already exist!'})
            }
        })
        const newUser = { username: req.body.username, password: req.body.password }
        Users.push(newUser)
        // req.session.user = newUser
        // res.redirect('/protected_page')
    }
});
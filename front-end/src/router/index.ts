// src/router/index.ts
import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import DestinataireView from '../views/DestinataireView.vue'
import Expediteur from '../views/Expediteur.vue'

const routes = [
  { path: '/', name: 'Login', component: LoginView },
  {path:'/destinataire',component:DestinataireView},
  {path:'/expediteur',component:Expediteur}
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router

// src/router/index.ts
import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import DestinataireView from '../views/DestinataireView.vue'
import Expediteur from '../views/Expediteur.vue'
import FormulaireMessage from '../views/FormulaireMessage.vue'
import MessageTexte from '../views/MessageTexte.vue'
import historiqueView from '../views/historiqueView.vue'

const routes = [
  { path: '/', name: 'Login', component: LoginView },
  {path:'/destinataire',component:DestinataireView},
  {path:'/expediteur',component:Expediteur},
  {path:'/formulaireMessage',component:FormulaireMessage},
  {path:'/messageTexte',component:MessageTexte},
  {path:'/historique',component:historiqueView}
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router

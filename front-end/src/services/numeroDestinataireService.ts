import api from './api'
import type { NumeroDestinataire } from '../types/NumeroDestinataire'

// Typage pour la réponse de la méthode POST
interface AddNumeroResponse {
  id: number
  valeur: string
  dateCreation: string
  nomPlateforme: string
  message: string
}

export default {
  // Récupérer tous les numéros
  getAll(): Promise<{ data: NumeroDestinataire[] }> {
    return api.get('/api/numero-destinataire')
  },

  // Ajouter un nouveau numéro avec une plateforme
  addNumero(data: { valeur: string; plateformeId: number }): Promise<AddNumeroResponse> {
    return api.post('/api/numero-destinataire', data)
  }
}

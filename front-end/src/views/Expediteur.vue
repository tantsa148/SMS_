<template>
  <div class="container mt-5">
    <!-- LOADING -->
    <div v-if="loading" class="text-center">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Chargement...</span>
      </div>
      <p class="mt-2 text-muted">Chargement de vos numéros...</p>
    </div>

    <!-- CARD -->
    <div v-else class="card">
      <div class="card-header d-flex justify-content-between align-items-center">
        <div class="card-title mb-0">Numéros Enregistrés</div>
        <button 
          class="btn btn-primary btn-sm"
          style="width: 100px"
          @click="showAddModal = true"
        >
          Ajouter
        </button>
      </div>

      <div class="card-body">
        <!-- MESSAGE API -->
        <div v-if="apiMessage" class="alert alert-success text-center">
          {{ apiMessage }}
        </div>

        <!-- AUCUN NUMÉRO -->
        <div v-if="numeros.length === 0" class="text-center py-4">
          <div class="text-muted mb-3">📱</div>
          <p class="text-muted mb-2">Aucun numéro trouvé</p>
        </div>

        <!-- TABLEAU -->
        <div v-else>
          <table class="table table-hover">
            <thead>
              <tr>
                <th>#</th>
                <th>Numéro</th>
                <th>Plateforme</th>
                <th>Créé le</th>
                <th>Utilisateur</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(row, index) in numeros" :key="row.id">
                <td>{{ index + 1 }}</td>
                <td>{{ row.valeur }}</td>
                <td>{{ row.plateformes.length > 0 ? row.plateformes.join(', ') : '-' }}</td>
                <td>{{ formatDate(row.dateCreation) }}</td>
                <td>{{ row.userUsername }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <!-- FOOTER -->
      <div v-if="numeros.length > 0" class="card-footer">
        <small class="text-muted">Total : {{ numeros.length }} numéro(s)</small>
      </div>
    </div>

    <!-- MODAL D'AJOUT -->
    <AddNumeroModal
      :show="showAddModal"
      @update:show="showAddModal = $event"
      @numero-added="handleNumeroAdded"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import NumeroExpediteurService from '../services/numeroExpediteurService'
import type { NumeroExpediteur } from '../types/NumeroExpediteur'
import AddNumeroModal from '../components/Numero.vue' // Chemin à ajuster
import '../assets/css/numero-form.css'

const loading = ref(true)
const numeros = ref<NumeroExpediteur[]>([])
const showAddModal = ref(false)
const apiMessage = ref('') // 🔹 message à afficher après ajout

const fetchData = async () => {
  try {
    const response = await NumeroExpediteurService.getAll()
    numeros.value = response
  } catch (err) {
    console.error('Erreur chargement des numéros :', err)
  } finally {
    loading.value = false
  }
}

// 🔹 Gérer l'ajout depuis le modal et afficher le message
const handleNumeroAdded = (newNumero: NumeroExpediteur) => {
  numeros.value.push(newNumero) // Ajouter le nouveau numéro à la liste
  apiMessage.value = newNumero.message // Afficher le message renvoyé par l'API

  // Supprimer le message après quelques secondes
  setTimeout(() => {
    apiMessage.value = ''
  }, 4000)
}

function formatDate(date: string) {
  return new Date(date).toLocaleString()
}

onMounted(fetchData)
</script>

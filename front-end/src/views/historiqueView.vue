<template>
  <div class="container mt-5">
    <!-- LOADING -->
    <div v-if="loading" class="text-center">
      <div class="spinner-border text-primary" role="status">
        <span class="visually-hidden">Chargement...</span>
      </div>
      <p class="mt-2 text-muted">Chargement de l'historique...</p>
    </div>

    <!-- NOTIFICATION FIXE À DROITE -->
    <div v-if="apiMessage" class="fixed-notification">
      <div class="notification-content">
        <div class="notification-header">
          <span class="notification-title"></span>
          <button class="notification-close" @click="apiMessage = ''">&times;</button>
        </div>
        <div class="notification-body">{{ apiMessage }}</div>
      </div>
    </div>

    <!-- CARD -->
    <div v-else class="card shadow">
      <div class="card-header d-flex justify-content-between align-items-center">
        <div class="card-title mb-0">Historique SMS</div>
      </div>

      <div class="card-body">
        <!-- AUCUN LOG -->
        <div v-if="historique.length === 0" class="text-center py-4">
          <div class="text-muted mb-3">📩</div>
          <p class="text-muted mb-2">Aucun historique trouvé</p>
        </div>

        <!-- TABLEAU -->
        <div v-else class="table-responsive">
          <table class="table table-hover">
            <thead>
              <tr>
                <th>#</th>
                <th>Destinataire</th>
                <th>Status</th>
                <th>Twilio Error Code</th>
                <th>Plateforme</th>
                <th>Date</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(log, index) in historique" :key="log.log_id">
                <td>{{ index + 1 }}</td>
                <td>{{ log.destinataire_numero }}</td>
                <td>{{ log.status }}</td>
                <td>{{ log.twilio_error_code || '-' }}</td>
                <td>{{ log.platform || '-' }}</td>
                <td>{{ formatDate(log.created_at) }}</td>
                <td>
                <button class="btn btn-sm btn-outline-secondary" @click="ouvrirModal(log.log_id)">
                <i class="fas fa-eye"></i>
                </button>
                </td>

              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div v-if="historique.length > 0" class="card-footer">
        <small class="text-muted">Total : {{ historique.length }} SMS</small>
      </div>
    </div>

    <!-- MODAL DETAIL -->
    <HistoriqueDetailModal
      :show="showDetailModal"
      :logDetail="selectedLogDetail"
      @update:show="showDetailModal = $event"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import { getHistoriqueSms } from '../services/historiqueService'
import { getHistoriqueSmsById } from '../services/historiqueDetailService'
import type { SmsResponseLog } from '../types/historique'
import type { historiqueDetail } from '../types/historiqueDetail'
import HistoriqueDetailModal from '../components/HistoriqueDetailModal.vue'

const loading = ref(true)
const historique = ref<SmsResponseLog[]>([])
const apiMessage = ref('')

let timeoutId: number | null = null

// Modal
const showDetailModal = ref(false)
const selectedLogDetail = ref<historiqueDetail | null>(null)

const fetchHistorique = async () => {
  try {
    historique.value = await getHistoriqueSms()
  } catch (error) {
    console.error('Impossible de charger l\'historique', error)
    apiMessage.value = 'Erreur lors du chargement de l\'historique.'
    if (timeoutId) clearTimeout(timeoutId)
    timeoutId = setTimeout(() => (apiMessage.value = ''), 3000)
  } finally {
    loading.value = false
  }
}

function formatDate(date: string) {
  return new Date(date).toLocaleString()
}

const ouvrirModal = async (id: number) => {
  try {
    selectedLogDetail.value = await getHistoriqueSmsById(id)
    showDetailModal.value = true
  } catch (err) {
    console.error('Erreur chargement détail SMS', err)
    apiMessage.value = 'Erreur lors du chargement du détail.'
    if (timeoutId) clearTimeout(timeoutId)
    timeoutId = setTimeout(() => (apiMessage.value = ''), 3000)
  }
}

onMounted(fetchHistorique)
onUnmounted(() => {
  if (timeoutId) clearTimeout(timeoutId)
})
</script>

<style scoped>
.table-responsive { overflow-x: auto; }
.fixed-notification {
  position: fixed;
  top: 80px;
  right: 20px;
  z-index: 999;
}
.notification-content {
  background: #f8f9fa;
  border: 1px solid #ccc;
  border-radius: 6px;
  padding: 10px;
  width: 300px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.2);
}
.notification-close {
  background: none;
  border: none;
  font-size: 18px;
  cursor: pointer;
}
</style>

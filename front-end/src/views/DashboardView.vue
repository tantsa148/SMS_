<template>
  <div class="container">
    <div class="page-inner">
      <div class="row">
        <!-- Enregistrés -->
        <div class="col-sm-6 col-md-3">
          <div class="card card-stats card-round">
            <div class="card-body d-flex align-items-center">
              <!-- Icône à gauche -->
              <div class="icon-square icon-primary me-3">
                <i class="fas fa-users"></i>
              </div>
              <!-- Texte à droite -->
              <div class="numbers">
                <p class="card-category mb-1">Enregistrés</p>
                <h4 class="card-title mb-0">
                  <span v-if="numeros.length > 0">{{ numeros.length }} numéro(s)</span>
                  <span v-else>0 numéro</span>
                </h4>
              </div>
            </div>
          </div>
        </div>

        <!-- Messages -->
        <div class="col-sm-6 col-md-3">
          <div class="card card-stats card-round">
            <div class="card-body d-flex align-items-center">
              <!-- Icône à gauche -->
              <div class="icon-square icon-primary me-3">
                <i class="fas fa-user-check"></i>
              </div>
              <!-- Texte à droite -->
              <div class="numbers">
                <p class="card-category mb-1">Messages Envoyés</p>
                <h4 class="card-title mb-0">
                  <span v-if="historique.length > 0">{{ historique.length }} message(s)</span>
                  <span v-else>0 message</span>
                </h4>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import numeroDestinataireService from '../services/numeroDestinataireService'
import type { NumeroDestinataire } from '../types/NumeroDestinataire'
import { getHistoriqueSms } from '../services/historiqueService'
import type { SmsResponseLog } from '../types/historique'
import "../assets/css/dashboard.css";

const loading = ref(true)
const numeros = ref<NumeroDestinataire[]>([])
const historique = ref<SmsResponseLog[]>([])

const fetchData = async () => {
  try {
    historique.value = await getHistoriqueSms()
    const response = await numeroDestinataireService.getAll()
    numeros.value = response.data
  } catch (err) {
    console.error('Erreur chargement des numéros :', err)
  } finally {
    loading.value = false
  }
}

onMounted(fetchData)
</script>


<template>
  <div class="modal-overlay" v-if="show" @click.self="closeModal">
    <div class="modal-content">
      <div class="modal-header">
        <h2>Ajouter un numéro</h2>
      </div>

      <form @submit.prevent="handleSubmit" class="modal-body">
        <!-- Champ Numéro + Indicatif -->
        <div class="form-group">
          <label for="numero">Numéro de téléphone :</label>
          <div class="phone-input-wrapper">
            <select v-model="selectedCountryCode" class="country-select">
              <option v-for="c in countries" :key="c.code" :value="c.code">
                {{ c.flag }} {{ c.name }} ({{ c.code }})
              </option>
            </select>

            <input
              type="text"
              id="numero"
              v-model="numeroLocal"
              placeholder="Ex: 612345678"
              required
              class="phone-input"
            />
          </div>
        </div>

        <!-- Sélecteur plateforme -->
        <div class="form-group">
          <label for="plateforme">Plateforme :</label>
          <select v-model="selectedPlateformeId" class="platform-select" required>
            <option value="">-- Sélectionnez une plateforme --</option>
            <option v-for="p in plateformes" :key="p.id" :value="p.id">
              {{ p.nomPlateforme }}
            </option>
          </select>
        </div>

        <!-- Boutons -->
        <div class="modal-actions">
          <button type="button" class="btn-cancel" @click="closeModal">
            Annuler
          </button>
          <button
            type="submit"
            :disabled="!isFormValid"
            :class="{ 'btn-disabled': !isFormValid }"
            class="btn-submit"
          >
            Ajouter
          </button>
        </div>
      </form>
    </div>

    <!-- MODALE DE CONFIRMATION -->
    <div class="modal-overlay" v-if="showConfirm" @click.self="showConfirm = false">
      <div class="modal-content">
        <div class="modal-header">
          <h2>Confirmer l'ajout</h2>
        </div>
        <div class="modal-body">
          <p>Voulez-vous vraiment ajouter ce numéro ?</p>
          <ul>
            <li><strong>Numéro :</strong> {{ fullNumero }}</li>
            <li><strong>Plateforme :</strong> {{ selectedPlateformeName }}</li>
          </ul>
        </div>
        <div class="modal-footer">
          <button class="btn-cancel" @click="showConfirm = false">Annuler</button>
          <button class="btn-submit" @click="confirmAdd" :disabled="adding">
            <span v-if="adding" class="spinner-border spinner-border-sm"></span>
            Confirmer
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, defineProps, defineEmits } from 'vue'
import countriesData from '../assets/countries.json' with { type: 'json' }
import { fetchPlateformes } from '../services/plateformeService'
import NumeroExpediteurService from '../services/numeroExpediteurService'

import type { Plateforme } from '../types/Plateforme'

// Props et emits
const props = defineProps<{ show: boolean }>()
const emit = defineEmits<{
  (e: 'update:show', value: boolean): void
  (e: 'numero-added', value: any): void
}>()

// Variables
const countries = ref(countriesData)
countries.value.sort((a, b) => a.name.localeCompare(b.name))

const selectedCountryCode = ref('+33')
const numeroLocal = ref('')

const plateformes = ref<Plateforme[]>([])
const selectedPlateformeId = ref<number | ''>('')

const showConfirm = ref(false)
const adding = ref(false)

// Validation
const isFormValid = computed(() =>
  numeroLocal.value.trim().length > 0 && selectedPlateformeId.value !== ''
)

// Numéro complet
const fullNumero = computed(() => {
  const local = numeroLocal.value.trim()
  return local ? `${selectedCountryCode.value}${local}` : ''
})

// Nom de la plateforme sélectionnée
const selectedPlateformeName = computed(() => {
  const p = plateformes.value.find(p => p.id === selectedPlateformeId.value)
  return p ? p.nomPlateforme : ''
})

// Chargement des plateformes
onMounted(async () => {
  try {
    plateformes.value = await fetchPlateformes()
  } catch (err) {
    console.error('Erreur chargement plateformes:', err)
  }
})

// Fermeture modal
const closeModal = () => {
  emit('update:show', false)
  resetForm()
}

const resetForm = () => {
  numeroLocal.value = ''
  selectedPlateformeId.value = ''
  selectedCountryCode.value = '+33'
}

// Soumission formulaire
const handleSubmit = () => {
  if (!isFormValid.value) return
  showConfirm.value = true
}

// Confirmation ajout
const confirmAdd = async () => {
  adding.value = true
  try {
    const data = {
      valeur: fullNumero.value,
      idPlateforme: selectedPlateformeId.value as number // <-- ici
    }
    const newNumero = await NumeroExpediteurService.add(data)
    emit('numero-added', newNumero)
    closeModal()
    showConfirm.value = false
  } catch (err) {
    console.error('Erreur ajout numéro:', err)
  } finally {
    adding.value = false
  }
}
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1050;
}

.modal-content {
  background: #fff;
  border-radius: 6px;
  max-width: 450px;
  width: 100%;
  padding: 1rem;
}

.modal-header, .modal-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.btn-cancel {
  background: #ccc;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.btn-submit {
  background: #007bff;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
}

.btn-disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.phone-input-wrapper {
  display: flex;
}

.country-select-wrapper {
  margin-right: 0.5rem;
}

.phone-input {
  flex: 1;
}

.platform-select {
  width: 100%;
}
</style>
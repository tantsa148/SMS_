<template>
  <nav class="navbar-header">
    <div class="container d-flex align-items-center justify-content-between">
      
      <!-- Logo ou autre élément à gauche -->
      <div class="navbar-brand">
        <!-- Votre logo ici -->
      </div>

      <!-- Groupe solde + utilisateur -->
      <div class="user-balance-group d-flex align-items-center gap-3">
        <!-- Solde Twilio -->
        <div class="twilio-balance-wrapper" v-if="balance">
          <div class="twilio-balance d-flex align-items-center gap-2">
            <i class="fa fa-wallet balance-icon"></i>
            <div class="balance-text">
              <span class="balance-amount">{{ balance.balance }} {{ balance.currency }}</span>
              <span class="balance-currency"></span>
            </div>
          </div>
        </div>

        <!-- Séparateur -->
        <div class="vertical-separator" v-if="balance"></div>

        <!-- Menu utilisateur -->
        <div class="user-profile position-relative">
          <button 
            class="btn btn-outline-secondary btn-sm user-btn d-flex align-items-center gap-2"
            @click="toggleUserMenu"
          >
            <i class="fa fa-user user-icon"></i>
            <span class="username">{{ currentUser ? currentUser.username : 'Utilisateur' }}</span>
            <i class="fa fa-chevron-down dropdown-arrow" :class="{ 'rotate': showUserMenu }"></i>
          </button>

          <div v-if="showUserMenu" class="user-dropdown shadow-dropdown">
            <div class="user-info">
              <div class="user-avatar">
                <i class="fa fa-user-circle"></i>
              </div>
              <div class="user-details">
                <p class="user-name">{{ currentUser ? currentUser.username : 'Utilisateur' }}</p>
                <p class="user-role" v-if="currentUser">{{ currentUser.role }}</p>
              </div>
            </div>
            <div class="user-menu">
              <a href="/expediteur" class="menu-item">
                <i class="fa fa-user"></i> Mes numéros
              </a>
              <a href="#" class="menu-item">
                <i class="fa fa-cog"></i> Paramètres
              </a>
              <a href="#" class="menu-item">
                <i class="fa fa-question-circle"></i> Aide
              </a>
              <div class="menu-divider"></div>
              <a href="#" class="menu-item logout" @click="logout">
                <i class="fa fa-sign-out"></i> Déconnexion
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { getCurrentUser } from '../services/userService';
import TwilioService from '../services/balanceService';
import type { TwilioBalance } from '../services/balanceService'; // <-- type-only import
import type { UserDTO } from '../types/user';
import '../assets/css/NavbarHeader.css';

const showUserMenu = ref(false);
const currentUser = ref<UserDTO | null>(null);

// Twilio balance
const balance = ref<TwilioBalance | null>(null);

function toggleUserMenu() {
  showUserMenu.value = !showUserMenu.value;
}

function logout() {
  console.log('Déconnexion');
  showUserMenu.value = false;
  localStorage.removeItem('token');
  window.location.reload();
}

// Récupération utilisateur connecté
onMounted(async () => {
  currentUser.value = await getCurrentUser();

  // Récupérer le solde Twilio
  try {
    balance.value = await TwilioService.getBalance();
  } catch (error) {
    console.error('Erreur récupération solde Twilio', error);
  }
});

// Fermer le menu si clic à l'extérieur
document.addEventListener('click', (e) => {
  const target = e.target as HTMLElement | null;
  if (!target) return;

  if (!target.closest('.user-profile')) showUserMenu.value = false;
});
</script>

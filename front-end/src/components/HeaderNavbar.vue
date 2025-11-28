<template>
  <nav class="navbar-header">
    <div class="container d-flex align-items-center justify-content-between">
      
      <!-- Barre de recherche -->
      <form class="search-form d-flex align-items-center" @submit.prevent="onSearch">
        <input
          type="text"
          v-model="query"
          class="form-control search-input"
          placeholder="Search ..."
        />
        <button class="btn btn-outline-secondary search-btn" type="submit">
          <i class="fa fa-search"></i>
        </button>
      </form>

      <!-- Notifications et profil utilisateur -->
      <div class="user-actions d-flex align-items-center gap-3">
        <!-- Menu utilisateur -->
        <!-- Menu utilisateur -->
<div class="user-profile position-relative">
  <button 
    class="btn btn-outline-secondary btn-sm user-btn d-flex align-items-center gap-2"
    @click="toggleUserMenu"
  >
    <i class="fa fa-user"></i>
    <span>{{ currentUser ? currentUser.username : 'Utilisateur' }}</span>
  </button>

  <div v-if="showUserMenu" class="user-dropdown">
    <div class="user-info shadow-dropdown">
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
import { ref, computed, onMounted } from 'vue';
import { getCurrentUser } from '../services/userService';
import type { UserDTO } from '../types/user';
import '../assets/css/NavbarHeader.css';

const query = ref('');
const showUserMenu = ref(false);
const currentUser = ref<UserDTO | null>(null);


// Fonctions
function onSearch() {
  console.log('Recherche:', query.value);
  query.value = '';
}


function toggleUserMenu() {
  showUserMenu.value = !showUserMenu.value;
}


function logout() {
  console.log('Déconnexion');
  showUserMenu.value = false;
  localStorage.removeItem('token'); // Supprime JWT
  window.location.reload();
}

// Récupération utilisateur connecté
onMounted(async () => {
  currentUser.value = await getCurrentUser();
});

// Fermer les menus si clic à l'extérieur
document.addEventListener('click', (e) => {
  const target = e.target as HTMLElement | null;
  if (!target) return;

  if (!target.closest('.user-profile')) showUserMenu.value = false;
});
</script>
<style>
/* NavbarHeader.css */

/* Header */
.navbar-header {
  position: fixed;
  top: 0;
  left: 250px;
  width: calc(100% - 250px);
  height: 60px;
  background-color: #fff;
  z-index: 900;
  display: flex;
  align-items: center;
  padding: 0 1rem;

  /* Ombre douce au lieu du border-bottom */
  border-bottom: none;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

/* Barre de recherche fusionnée avec le bouton */
.search-form {
  display: flex;
  align-items: center;
  flex: 1;
  max-width: 300px;
  border: 1px solid #ced4da;
  border-radius: 6px;
  overflow: hidden;
}

.search-input {
  border: none;
  outline: none;
  padding: 0.375rem 0.75rem;
  flex: 1;
}

.search-btn {
  border: none;
  background-color: #f8f9fa;
  padding: 0.375rem 0.75rem;
  cursor: pointer;
  transition: background-color 0.2s ease;
}

.search-btn:hover {
  background-color: #e2e6ea;
}

/* Container des actions utilisateur */
.user-actions {
  margin-left: auto;
  display: flex;
  align-items: center;
  gap: 1rem;
}

/* Bouton utilisateur avec nom à côté */
.user-btn {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.9rem;
  padding: 0.4rem 0.6rem;
  border: 1px solid #dee2e6;
  border-radius: 6px;
  transition: all 0.2s ease;
  background-color: #fff;
}

.user-btn:hover {
  background-color: #f8f9fa;
  border-color: #6c757d;
}

/* Dropdown utilisateur avec ombre */
.user-dropdown {
  position: absolute;
  top: 100%;
  right: 0;
  width: 250px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 8px 20px rgba(0,0,0,0.15);
  z-index: 1000;
  margin-top: 8px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 1rem;
  /* petite ombre interne sous le header du dropdown */
  box-shadow: 0 2px 4px rgba(0,0,0,0.05);
  border-radius: 6px;
}

.user-avatar {
  font-size: 2rem;
  color: #6b7280;
}

.user-details {
  flex: 1;
}

.user-name {
  margin: 0;
  font-weight: 600;
  color: #1f2937;
  font-size: 0.9rem;
}

.user-role {
  font-size: 0.75rem;
  color: #6b7280;
  margin: 0;
}

/* Menu utilisateur */
.user-menu {
  padding: 0.5rem 0;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 0.75rem 1rem;
  color: #374151;
  text-decoration: none;
  font-size: 0.875rem;
  transition: background-color 0.2s ease;
  gap: 0.75rem;
}

.menu-item:hover {
  background-color: #f8fafc;
  color: #1f2937;
}

.menu-item i {
  width: 16px;
  text-align: center;
  color: #6b7280;
}

.menu-item.logout {
  color: #ef4444;
}

.menu-item.logout:hover {
  background-color: #fef2f2;
  color: #dc2626;
}

/* Divider du menu */
.menu-divider {
  height: 1px;
  background-color: transparent; /* plus de ligne, on garde l’ombre en haut */
  margin: 0.5rem 0;
}

</style>
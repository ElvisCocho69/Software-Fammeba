<script setup>
import UserBioPanel from '@/views/user/UserBioPanel.vue'
import UserTabSecurity from '@/views/user/UserTabSecurity.vue'

const route = useRoute('apps-user-view-id')
const userTab = ref(null)
const userData = ref(null)
const error = ref(null)

// Obtener los datos del usuario desde el store o props
const fetchUserData = () => {
  try {
    // Obtener la lista de usuarios del localStorage
    const users = JSON.parse(localStorage.getItem('users') || '[]')
    const user = users.find(u => u.id === parseInt(route.params.id))
    
    if (user) {
      userData.value = user
    } else {
      error.value = 'Usuario no encontrado'
    }
  } catch (err) {
    error.value = 'Error al cargar los datos del usuario'
    console.error('Error:', err)
  }
}

// Cargar datos al montar el componente
onMounted(() => {
  fetchUserData()
})

const tabs = [  
  {
    icon: 'ri-lock-2-line',
    title: 'Seguridad',
  }  
]
</script>

<template>
  <VRow v-if="userData">
    <VCol
      cols="12"
      md="5"
      lg="4"
    >
      <UserBioPanel :user-data="userData" />
    </VCol>

    <VCol
      cols="12"
      md="7"
      lg="8"
    >
      <VTabs
        v-model="userTab"
        class="v-tabs-pill"
      >
        <VTab
          v-for="tab in tabs"
          :key="tab.icon"
        >
          <VIcon
            start
            :icon="tab.icon"
          />
          <span>{{ tab.title }}</span>
        </VTab>
      </VTabs>

      <VWindow
        v-model="userTab"
        class="mt-6 disable-tab-transition"
        :touch="false"
      >              
        <VWindowItem>
          <UserTabSecurity />
        </VWindowItem>       
      </VWindow>
    </VCol>
  </VRow>
  <div v-else-if="error">
    <VAlert
      type="error"
      variant="tonal"
    >
      {{ error }}
    </VAlert>
  </div>
  <div v-else>
    <VProgressCircular
      indeterminate
      color="primary"
    />
  </div>
</template>

<script setup>
import { ref, watch, computed, onMounted } from 'vue'
import { $api } from '@/utils/api'

const props = defineProps({
  isDialogVisible: {
    type: Boolean,
    required: true,
  },
  design: {
    type: Object,
    required: true,
  },
})

const emit = defineEmits([
  'update:isDialogVisible',
])

// Variable local para controlar el diálogo
const isDialogOpen = ref(false)
const imageBlobUrl = ref('')
const isFullscreenImageVisible = ref(false)

// Función para obtener la imagen
const getImage = async () => {
  if (!props.design?.imagepath) return
  
  try {
    const fileName = props.design.imagepath.split('/').pop()
    const response = await $api(`/files/designs/${fileName}`, {
      method: 'GET',
      responseType: 'blob',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    
    // Crear URL del blob
    imageBlobUrl.value = URL.createObjectURL(response)
  } catch (error) {
    console.error('Error al obtener la imagen:', error)
    imageBlobUrl.value = ''
  }
}

// Observar cambios en el diseño
watch(() => props.design, () => {
  if (props.design?.imagepath) {
    getImage()
  }
}, { immediate: true })

// Limpiar URL del blob cuando se cierra el diálogo
watch(isDialogOpen, (newValue) => {
  if (!newValue && imageBlobUrl.value) {
    URL.revokeObjectURL(imageBlobUrl.value)
    imageBlobUrl.value = ''
  }
  if (newValue !== props.isDialogVisible) {
    emit('update:isDialogVisible', newValue)
  }
})

// Sincronizar con la prop
watch(() => props.isDialogVisible, (newValue) => {
  isDialogOpen.value = newValue
})

const closeDialog = () => {
  isDialogOpen.value = false
}

// Limpiar recursos al desmontar
onMounted(() => {
  if (props.design?.imagepath) {
    getImage()
  }
})

const openFullscreenImage = () => {
  isFullscreenImageVisible.value = true
}

const closeFullscreenImage = () => {
  isFullscreenImageVisible.value = false
}
</script>

<template>
  <VDialog
    v-model="isDialogOpen"
    max-width="800"
    persistent
  >
    <VCard>
      <div class="text-center mt-5">
        <h4 class="text-h4 mb-2">
          Detalles del Diseño
        </h4>          
      </div>

      <VCardText class="pa-6">
        <VRow>
          <VCol cols="12" md="6">
            <div class="d-flex flex-column gap-4">
              <div>
                <h6 class="text-h6 mb-2">Nombre:</h6>
                <p class="text-body-1">{{ design?.name }}</p>
              </div>

              <div>
                <h6 class="text-h6 mb-2">Descripción:</h6>
                <p class="text-body-1">{{ design?.description }}</p>
              </div>

              <div>
                <h6 class="text-h6 mb-2">Versión:</h6>
                <p class="text-body-1">{{ design?.version }}</p>
              </div>
            </div>
          </VCol>

          <VCol cols="12" md="6">
            <div class="d-flex justify-center">
              <VImg
                v-if="imageBlobUrl"
                :src="imageBlobUrl"
                width="300"
                height="300"
                cover
                class="rounded cursor-pointer"
                @click="openFullscreenImage"
              />
              <VProgressCircular
                v-else
                indeterminate
                color="primary"
              />
            </div>
          </VCol>
        </VRow>
      </VCardText>

      <VCardText class="d-flex justify-end flex-wrap gap-4 pa-6">
        <VBtn
          variant="outlined"
          color="error"
          @click="closeDialog"
          prepend-icon="ri-close-fill"          
        >
          Cerrar
        </VBtn>
      </VCardText>
    </VCard>
  </VDialog>

  <!-- Diálogo de imagen a pantalla completa -->
  <VDialog
    v-model="isFullscreenImageVisible"
    fullscreen
    transition="dialog-bottom-transition"
  >
    <VCard class="fullscreen-image-card">
      <VToolbar
        color="#1A237E"
        class="fullscreen-image-toolbar"
      >
        <VBtn
          icon
          @click="closeFullscreenImage"
        >
          <VIcon>ri-close-line</VIcon>
        </VBtn>
        <VToolbarTitle>Vista completa</VToolbarTitle>
      </VToolbar>

      <VCardText class="fullscreen-image-container">
        <VImg
          v-if="imageBlobUrl"
          :src="imageBlobUrl"
          contain
          class="fullscreen-image"
        />
      </VCardText>
    </VCard>
  </VDialog>
</template>

<style lang="scss" scoped>
.v-card {
  border-radius: 8px;
}

.v-img {
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.cursor-pointer {
  cursor: pointer;
}

.fullscreen-image-card {
  background-color: rgba(0, 0, 0, 0.9);
}

.fullscreen-image-toolbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1;
}

.fullscreen-image-container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  padding: 0;
  margin-top: 64px; // Altura de la toolbar
}

.fullscreen-image {
  max-width: 100%;
  max-height: calc(100vh - 64px);
  object-fit: contain;
}
</style> 
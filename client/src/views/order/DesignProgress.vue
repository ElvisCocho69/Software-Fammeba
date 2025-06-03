<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { $api } from '@/utils/api'

const route = useRoute()
const router = useRouter()
const design = ref(null)
const loading = ref(true)
const error = ref(null)

// Calcular el progreso total
const calculateProgress = computed(() => {
  if (!design.value) return 0
  
  const totalSteps = 5 // Total de pasos en el proceso
  const completedSteps = [
    design.value.designApproved,
    design.value.materialSelected,
    design.value.productionStarted,
    design.value.qualityCheck,
    design.value.completed
  ].filter(Boolean).length

  return (completedSteps / totalSteps) * 100
})

// Definir los pasos del timeline
const timelineSteps = [
  {
    title: 'Diseño Aprobado',
    icon: 'ri-check-double-line',
    color: 'success',
    completed: computed(() => design.value?.designApproved),
    date: '2 meses atrás',
    description: 'El diseño ha sido aprobado por el cliente y está listo para continuar con el siguiente paso.'
  },
  {
    title: 'Material Seleccionado',
    icon: 'ri-shopping-cart-line',
    color: 'info',
    completed: computed(() => design.value?.materialSelected),
    date: '1 mes atrás',
    description: 'Los materiales necesarios han sido seleccionados y están listos para la producción.'
  },
  {
    title: 'Producción Iniciada',
    icon: 'ri-tools-line',
    color: 'warning',
    completed: computed(() => design.value?.productionStarted),
    date: '2 semanas atrás',
    description: 'El proceso de producción ha comenzado según lo planificado.'
  },
  {
    title: 'Control de Calidad',
    icon: 'ri-shield-check-line',
    color: 'primary',
    completed: computed(() => design.value?.qualityCheck),
    date: '1 semana atrás',
    description: 'Se está realizando el control de calidad del producto.'
  },
  {
    title: 'Completado',
    icon: 'ri-flag-line',
    color: 'success',
    completed: computed(() => design.value?.completed),
    date: 'Hoy',
    description: 'El proyecto ha sido completado exitosamente.'
  }
]

const fetchDesignData = async () => {
  try {
    const structureId = route.params.structureId
    if (!structureId) {
      throw new Error('No se proporcionó un ID de estructura')
    }

    const response = await $api(`/designs/structure/${structureId}`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    
    design.value = response
  } catch (err) {
    error.value = err.message || 'Error al cargar los datos del diseño'
    console.error('Error:', err)
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchDesignData()
})

const goBack = () => {
  router.back()
}
</script>

<template>
  <VRow>
    <VCol cols="12">
      <VCard class="pa-6">
        <!-- Loading State -->
        <div v-if="loading" class="d-flex justify-center align-center pa-6">
          <VProgressCircular indeterminate />
        </div>

        <!-- Error State -->
        <VAlert
          v-if="error"
          type="error"
          class="ma-4"
        >
          {{ error }}
        </VAlert>

        <!-- Content -->
        <template v-if="design && !loading">
          <!-- Header -->
          <div class="d-flex justify-space-between align-center mb-6">
            <h5 class="text-h5">Progreso de la Estructura</h5>
            <VBtn
              prepend-icon="ri-arrow-left-line"
              @click="goBack"
            >
              Volver
            </VBtn>
          </div>

          <!-- Barra de progreso general -->
          <div class="mb-6">
            <div class="d-flex justify-space-between align-center mb-2">
              <span class="text-body-1">Progreso Total</span>
              <span class="text-body-1">{{ Math.round(calculateProgress) }}%</span>
            </div>
            <VProgressLinear
              :model-value="calculateProgress"
              color="primary"
              height="10"
              rounded
            />
          </div>

          <!-- Timeline -->
          <VTimeline
            align="start"
            line-inset="16"
            truncate-line="start"
            justify="center"
            :density="$vuetify.display.smAndDown ? 'compact' : 'default'"
            class="mt-10"
          >
            <VTimelineItem
              v-for="(step, index) in timelineSteps"
              :key="index"
              fill-dot
              size="small"
            >
              <template #opposite>
                <span class="app-timeline-meta">
                  {{ step.date }}
                </span>
              </template>
              <template #icon>
                <div class="v-timeline-avatar-wrapper rounded-circle">
                  <VAvatar
                    size="32"
                    :color="step.completed ? step.color : 'grey'"
                    variant="tonal"
                  >
                    <VIcon
                      :icon="step.icon"
                      size="20"
                    />
                  </VAvatar>
                </div>
              </template>
              <VCard class="mb-10 mt-n4">
                <VCardItem class="pb-4">
                  <VCardTitle>{{ step.title }}</VCardTitle>
                </VCardItem>
                <VCardText>
                  <p class="app-timeline-text mb-3">
                    {{ step.description }}
                  </p>
                  <div v-if="step.completed" class="d-flex align-center">
                    <VIcon
                      icon="ri-check-line"
                      color="success"
                      class="me-2"
                    />
                    <span class="text-success">Completado</span>
                  </div>
                  <div v-else class="d-flex align-center">
                    <VIcon
                      icon="ri-time-line"
                      color="warning"
                      class="me-2"
                    />
                    <span class="text-warning">Pendiente</span>
                  </div>
                </VCardText>
              </VCard>
            </VTimelineItem>
          </VTimeline>

          <!-- Observaciones -->
          <div v-if="design?.progressNotes" class="mt-6">
            <h6 class="text-h6 mb-2">Observaciones del Progreso</h6>
            <p class="text-body-1">{{ design.progressNotes }}</p>
          </div>
        </template>
      </VCard>
    </VCol>
  </VRow>
</template>

<style lang="scss">
.v-timeline-avatar-wrapper {
  background-color: rgb(var(--v-theme-surface));
}

.app-timeline-meta {
  font-size: 0.875rem;
  color: rgba(var(--v-theme-on-surface), var(--v-medium-emphasis-opacity));
}

.app-timeline-text {
  color: rgba(var(--v-theme-on-surface), var(--v-medium-emphasis-opacity));
}
</style> 
<script setup>
import { PERMISOS } from '@/utils/constants'
import { VCheckbox, VTextField } from 'vuetify/components'

const props = defineProps({
  isDialogVisible: {
    type: Boolean,
    required: true,
  },
})

const emit = defineEmits(['update:isDialogVisible'])

const dialogVisibleUpdate = val => {
  emit('update:isDialogVisible', val)
}

const LIST_PERMISSION = PERMISOS;

</script>

<template>
  <VDialog
    :model-value="props.isDialogVisible"
    max-width="750"
    @update:model-value="dialogVisibleUpdate"
  >
    <VCard class="refer-and-earn-dialog pa-3 pa-sm-11">
      <!-- 👉 dialog close btn -->
      <DialogCloseBtn
        variant="text"
        size="default"
        @click="emit('update:isDialogVisible', false)"
      />

      <VCardText class="pa-5">
        <div class="mb-6">
          <h4 class="text-h4 text-center mb-2">
            Añadir Rol
          </h4>
          <!--
            <p class="text-sm-body-1 text-center">
                Supported payment methods
            </p>
          -->
        </div>

        <VTextField
            label="Rol"
            placeholder="Ejemplo: Administrador"
        />        
        
      </VCardText>

      <VCardText class="pa-5">
        <VBtn color="primary mb-4">
            Crear
        </VBtn>
        <VTable>
            <thead>
                <tr>
                    <th class="text-uppercase">
                        Módulo
                    </th>
                    <th class="text-uppercase">
                        Permiso
                    </th>
                    
                </tr>
            </thead>

            <tbody>
                <tr
                    v-for="item in LIST_PERMISSION"
                    :key="index"
                >
                    <td>
                        {{ item.name }}
                    </td>
                    <td>
                       <ul>
                            <li v-for="(permiso, index2) in item.permisos" :key="index2" style="list-style: none;">
                                <VCheckbox
                                    :label="permiso.name"
                                    :value="permiso.permiso"
                                />
                            </li>
                       </ul>
                    </td>                    
                </tr>
            </tbody>
        </VTable>        
      </VCardText>

       
    </VCard>
  </VDialog>
</template>

<style lang="scss">
.refer-link-input {
  .v-field--appended {
    padding-inline-end: 0;
  }

  .v-field__append-inner {
    padding-block-start: 0.125rem;
  }
}
</style>

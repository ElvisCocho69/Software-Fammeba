<script>
import AddRoleDialog from '@/components/fammeba/role/AddRoleDialog.vue';
import data from '@/views/js/datatable'
import { VBtn, VCard, VCardText, VDataTable, VSpacer, VTextField } from 'vuetify/components';

const headers = [
    { title: 'ID', key: 'id' },
    { title: 'NAME', key: 'name' },
    { title: 'EMAIL', key: 'email' },
    { title: 'DATE', key: 'date' },
    { title: 'EXPERIENCE', key: 'experience' },
    { title: 'AGE', key: 'age' }
];

export default {
    data() {
        return {
            headers,
            data ,
            isAddRoleDialogVisible: false 
        };
    }
};
</script>

<template>
    <div>

        <VCard title="Roles y Permisos">

            <VCardText class="d-flex flex-wrap gap-4">
                <div class="d-flex align-center">
                    <VTextField
                        
                        placeholder="Buscar Rol"
                        style="inline-size: 300px;"
                        density="compact"
                        class="me-3"
                    >
                    </VTextField>
                </div>
                <VSpacer></VSpacer>
                <div class="d-flex gap-x-4 align-center">                    
                    <VBtn
                        color="primary"
                        prepend-icon="ri-add-line"
                        @click="isAddRoleDialogVisible = !isAddRoleDialogVisible"
                    >
                        Añadir Rol
                    </VBtn>
                </div>
            </VCardText>

            <VDataTable :headers="headers" :items="data" :items-per-page="5" class="text-no-wrap mt-5">
                <template #item.id="{ item }">
                    <span class="text-h6">{{ item.id }}</span>
                </template>
                <template #item.name="{ item }">
                    <span>{{ item.fullName }}</span>
                </template>
                <template #item.email="{ item }">
                    <span>{{ item.email }}</span>
                </template>
                <template #item.date="{ item }">
                    <span>{{ item.startDate }}</span>
                </template>
                <template #item.experience="{ item }">
                    <span>{{ item.experience }}</span>
                </template>
                <template #item.age="{ item }">
                    <span>{{ item.age }}</span>
                </template>
            </VDataTable>

            <AddRoleDialog v-model:is-dialog-visible="isAddRoleDialogVisible" />

        </VCard>

    </div>
</template>

<script>
import { ref, onMounted } from 'vue';

const headers = [
    { title: 'ID', key: 'id' },
    { title: 'ROL', key: 'name' },
    { title: 'PERMISOS', key: 'permissions' },
    { title: 'OPCIONES', key: 'actions' }
];

export default {
    setup() {
        const data = ref([]);
        const isAddRoleDialogVisible = ref(false);

        // Función para obtener los roles
        const list = async () => {
            try {
                const resp = await $api('/roles', {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    }
                });
                data.value = resp.content || [];
            } catch (error) {
                console.error('Error al obtener roles:', error);
            }
        };

        // Llamada a la función al montar el componente
        onMounted(() => {
            list();
        });

        return {
            headers,
            data,
            isAddRoleDialogVisible
        };
    }
};
</script>

<template>
    <div>

        <VCard title="Roles y Permisos">

            <VCardText class="d-flex flex-wrap gap-4">
                <div class="d-flex align-center">
                    <VTextField placeholder="Buscar Rol" style="inline-size: 300px;" density="compact" class="me-3">
                    </VTextField>
                </div>
                <VSpacer></VSpacer>
                <div class="d-flex gap-x-4 align-center">
                    <VBtn color="primary" prepend-icon="ri-add-line"
                        @click="isAddRoleDialogVisible = !isAddRoleDialogVisible">
                        Añadir Rol
                    </VBtn>
                </div>
            </VCardText>

            <VDataTable :headers="headers" :items="data" :items-per-page="5" class="text-no-wrap mt-5">
                <template #item.id="{ item }">
                    <span class="text-h6">{{ item.id }}</span>
                </template>

                <template #item.name="{ item }">
                    <span>{{ item.name }}</span>
                </template>

                <template #item.permissions="{ item }">
                    <ul>
                        <li v-for="(permiso, index) in item.permissions" :key="index">
                            {{ permiso }}
                        </li>
                    </ul>
                </template>

                <template #item.actions="{ item }">
                    <div class="d-flex gap-1">
                        <IconBtn size="small" @click="editItem(item)">
                            <VIcon icon="ri-pencil-line" />
                        </IconBtn>
                        <IconBtn size="small" @click="deleteItem(item)">
                            <VIcon icon="ri-delete-bin-line" />
                        </IconBtn>
                    </div>
                </template>
            </VDataTable>


            <AddRoleDialog v-model:is-dialog-visible="isAddRoleDialogVisible" />

        </VCard>

    </div>
</template>

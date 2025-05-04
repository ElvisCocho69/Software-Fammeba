<script>
import { ref, onMounted } from 'vue';
import { $api } from '@/utils/api';
import AddRoleDialog from '@/components/fammeba/role/AddRoleDialog.vue';
import EditRoleDialog from '@/components/fammeba/role/EditRoleDialog.vue';
import DeleteRoleDialog from '@/components/fammeba/role/DeleteRoleDialog.vue';

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
        const isEditRoleDialogVisible = ref(false);
        const selectedRole = ref(null);
        const isDeleteDialogVisible = ref(false);
        const roleToDelete = ref(null);

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

        const editItem = (item) => {
            selectedRole.value = item;
            isEditRoleDialogVisible.value = true;
        };

        const deleteItem = async (item) => {
            if (confirm('¿Está seguro de eliminar este rol?')) {
                try {
                    await $api(`/roles/${item.id}`, {
                        method: 'DELETE',
                        headers: {
                            'Authorization': `Bearer ${localStorage.getItem('token')}`
                        }
                    });
                    await list(); // Recargar la lista después de eliminar
                } catch (error) {
                    console.error('Error al eliminar el rol:', error);
                }
            }
        };

        const handleRoleUpdated = () => {
            list(); // Recargar la lista después de actualizar
        };

        const openDeleteDialog = (role) => {
            roleToDelete.value = role;
            isDeleteDialogVisible.value = true;
        };

        const handleRoleDeleted = () => {
            isDeleteDialogVisible.value = false;
            list();
        };

        // Llamada a la función al montar el componente
        onMounted(() => {
            list();
        });

        return {
            headers,
            data,
            isAddRoleDialogVisible,
            isEditRoleDialogVisible,
            selectedRole,
            list,
            editItem,
            deleteItem,
            handleRoleUpdated,
            isDeleteDialogVisible,
            roleToDelete,
            openDeleteDialog,
            handleRoleDeleted
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
                        <IconBtn size="small" @click="openDeleteDialog(item)">
                            <VIcon icon="ri-delete-bin-line" />
                        </IconBtn>
                    </div>
                </template>
            </VDataTable>


            <AddRoleDialog v-model:is-dialog-visible="isAddRoleDialogVisible" @role-added="list" />
            <EditRoleDialog 
                v-model:is-dialog-visible="isEditRoleDialogVisible" 
                :role-data="selectedRole"
                @role-updated="handleRoleUpdated"
            />
            <DeleteRoleDialog
                v-model:is-dialog-visible="isDeleteDialogVisible"
                :role="roleToDelete"
                @role-deleted="handleRoleDeleted"
            />

        </VCard>

    </div>
</template>

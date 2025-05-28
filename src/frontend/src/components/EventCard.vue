<template>
	<v-card class="mx-auto" fixed-width-card>
		<v-img height="200px" :src="computedImageSrc" cover></v-img>

		<v-card-title class="title-wrap">{{ title }}</v-card-title>

		<v-card-subtitle class="text-caption text-grey">
			Posted by 
			<template v-if="username">
				<router-link
					:to="{ name: 'Profile', params: { username: username } }"
					class="clickable-username"
				>
					@{{ username }}
				</router-link>
			</template>
			<template v-else>
				<span class="text-grey">Unknown</span>
			</template>
			&mdash; {{ location }}
		</v-card-subtitle>

		<v-card-actions>
			<v-btn color="orange-darken-3" text variant="tonal" @click="goToEventDetails">Explore</v-btn>
			<v-btn
  				:color="joined ? 'red-darken-3' : 'green-darken-3'"
  				text
  				variant="tonal"
  				@click="joined ? unjoinEvent() : joinEvent()"
			>
  				{{ joined ? 'Unjoin' : 'Join' }}
			</v-btn>

			<!-- DELETE BUTTON FOR ADMIN -->
			<v-btn
				v-if="props.isAdmin"
				color="red-darken-4"
				text
				variant="tonal"
				:loading="isDeleting"
				:disabled="isDeleting"
				@click="showDeleteDialog = true"
				>
					<template v-if="isDeleting">
						<v-progress-circular
							indeterminate
							size="20"
							width="2"
							color="white"
							class="mr-2"
						></v-progress-circular>
						Deleting...
					</template>
					<template v-else>
						Delete
					</template>
				</v-btn>

			<v-spacer></v-spacer>
			<v-btn icon @click="toggleContent">
				<v-icon>{{ show ? 'mdi-chevron-up' : 'mdi-chevron-down' }}</v-icon>
			</v-btn>
		</v-card-actions>

		<!-- Delete dialog for ADMIN -->
		<v-dialog v-model="showDeleteDialog" max-width="500">
			<v-card>
				<v-card-title>Delete "{{ props.title }}"?</v-card-title>
				<v-card-text>
					<v-textarea
						v-model="deletionReason"
						label="Reason for deletion"
						auto-grow
						rows="3"
						:rules="[reasonRule]"
						required
						:error-messages="reasonError"
					/>
				</v-card-text>
				<v-card-actions>
					<v-spacer/>
					<v-btn text @click="showDeleteDialog = false" variant="tonal" :disabled="isDeleting">Cancel</v-btn>
					<v-btn 
						color="red-darken-3" 
						@click="submitDelete" 
						variant="tonal"
						:loading="isDeleting"
						:disabled="isDeleting">
						<template v-if="isDeleting">
							<v-progress-circular
								indeterminate
								size="20"
								width="2"
								color="white"
								class="mr-2"
							></v-progress-circular>
							Deleting...
						</template>
						<template v-else>
							Confirm Delete
						</template>
					</v-btn>
				</v-card-actions>
			</v-card>
		</v-dialog>

		<v-expand-transition>
			<div v-show="show">
				<v-divider></v-divider>
				<v-card-text>
					<p><strong>Category: </strong> {{ category }}</p>
					<p><strong>Date:</strong> {{ formattedDate }}</p>
					<p><strong>Time: </strong> {{  formattedTime }}</p>
					<p>{{ description }}</p>
				</v-card-text>
			</div>
		</v-expand-transition>
	</v-card>

	<v-snackbar
		v-model="snackbar"
		:timeout="5000"
		color="snackbarColor"
		location="top"
	>
		{{ snackbarMessage }}
	</v-snackbar>
</template>

<script setup>
import { ref, computed, getCurrentInstance} from 'vue'
import { useRouter } from 'vue-router'
import { getUsernameFromToken } from '@/utils/jwt'
import { onMounted } from 'vue'


const { appContext } = getCurrentInstance()
const axios = appContext.config.globalProperties.$http


const props = defineProps({
	id: Number,
	title: String,
	category: String,
	description: String,
	location: String,
	time: String,
	imageSrc: String,
	username: String, // Creator of the post
	joined: Boolean,
	isAdmin: Boolean
})

const computedImageSrc = computed(() => {
	if (!props.imageSrc) return ''
	// If imageSrc already has http, return it as is. Else, prepend localhost base.
	return props.imageSrc.startsWith('http')
		? props.imageSrc
		: `http://localhost:80${props.imageSrc}`
})

const show = ref(false)
const joined = ref(props.joined || false)


const toggleContent = () => {
	show.value = !show.value
}

const router = useRouter();

function goToEventDetails() {
	router.push({ name: 'EventDetails', params: { id: props.id } });
}


onMounted(async () => {
	const username = getUsernameFromToken()
	try {
		const { data } = await axios.get(`http://localhost/api/events/${props.id}/is-joined`, {
			params: { username }
		})
		joined.value = data
	} catch (error) {
		console.error('Error checking join status:', error.response)
	}
})

async function joinEvent() {
	const username = getUsernameFromToken()
	if (!username) {
		alert('Please log in to join the event.')
		return
	}

	try {
		await axios.post(`http://localhost/api/events/${props.id}/join`, null, {
			params: { username }
		})
		joined.value = true
		emit('update:joined', { id: props.id, joined: true})
	} catch (error) {
		console.error('Join event error:', error.response);
		alert(error.response?.data || 'Failed to join the event')
	}
}

async function unjoinEvent() {
  const username = getUsernameFromToken()
  if (!username) {
    alert('Please log in to unjoin the event.')
    return
  }

  try {
    await axios.delete(`http://localhost/api/events/${props.id}/unjoin`, {
      params: { username }
    })
    joined.value = false
	emit('update:joined', { id: props.id, joined: false})
  } catch (error) {
    console.error('Unjoin event error:', error.response)
    alert(error.response?.data || 'Failed to unjoin the event')
  }
}

const formattedTime = computed( () => {
	if (!props.time) return ''
	const date = new Date(props.time)
	return date.toLocaleTimeString([], {  hour: '2-digit', minute: '2-digit' })
})

const formattedDate = computed(() => {
	if (!props.time) return ''
	const date = new Date(props.time)
	return date.toLocaleDateString([], { year: 'numeric', month: 'long', day: 'numeric' })
})

function goToUserProfile() {
  router.push({ name: 'Profile', params: { username: props.username } });
}

const emit = defineEmits(['update:joined', 'event-deleted'])

// Deletion logic for ADMIN
const showDeleteDialog = ref(false)
const deletionReason = ref('')
const isDeleting = ref(false)
const reasonError = ref('')

const snackbar = ref(false)
const snackbarMessage = ref('')
const snackbarColor = ref('')

const reasonRule = (v) => {
  if (!v) return 'Reason for deletion is required'
  return true
}

async function submitDelete() {
	reasonError.value = ''
	const validation = reasonRule(deletionReason.value)
	if (validation !== true) {
		reasonError.value = validation
		return
	}

	isDeleting.value = true
	try {
		await axios.delete(`/api/events/delete-event-admin/${props.id}`, {
			data: { reason: deletionReason.value } 
		})
		emit('event-deleted', props.id)
		showDeleteDialog.value = false
		deletionReason.value = ''

		snackbarMessage.value = 'Event deleted successfully.'
		snackbarColor.value = "green"
		snackbar.value = true
	} catch (err) {
		console.error('Delete failed:', err)
		snackbarMessage.value = "Failed to delete event."
		snackbarColor.value = "red"
		snackbar.value = true
	} finally {
		isDeleting.value = false
	}
}
</script>





<style scoped>
.fixed-width-card
{
	width: 250px;
}

.title-wrap
{
	white-space: normal;
	overflow: visible;
	word-wrap: break-word;
}

.clickable-username {
  color: #1976d2;
  cursor: pointer;
  text-decoration: underline;
  margin-left: 4px;
  margin-right: 4px;
}
</style>

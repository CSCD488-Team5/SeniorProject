<template>
	<v-card class="mx-auto">
		<v-img height="200px" :src="imageSrc" cover></v-img>
		<v-card-title class="title-wrap">{{ title }}</v-card-title>
		<v-card-subtitle>{{ subtitle }}</v-card-subtitle>

		<v-card-actions>
			<v-btn color="orange-darken-3" text variant="tonal" @click="goToEventDetails">Explore</v-btn>
			<v-spacer></v-spacer>
			<v-btn icon @click="toggleContent">
				<v-icon>{{ show ? 'mdi-chevron-up' : 'mdi-chevron-down' }}</v-icon>
			</v-btn>
		</v-card-actions>

		<v-expand-transition>
			<div v-show="show">
				<v-divider></v-divider>
				<v-card-text>
					{{ content }}
				</v-card-text>
			</div>
		</v-expand-transition>
	</v-card>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const props = defineProps({
	imageSrc: { type: String, required: true },
	title: { type: String, required: true },
	subtitle: { type: String, required: true },
	content: { type: String, required: true },
	id: Number
})

const show = ref(false)
const toggleContent = () => {
	show.value = !show.value
}

const router = useRouter();

function goToEventDetails() {
	router.push({ name: 'EventDetails', params: { id: props.id } });
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
</style>

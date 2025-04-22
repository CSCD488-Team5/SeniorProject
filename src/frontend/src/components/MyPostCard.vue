<template>
  <v-card
      class="background-card"
      elevation="4"
  >
    <v-img
        :src="image"
        class="background-image"
        gradient="to top, rgba(0,0,0,0.7), rgba(0,0,0,0.3)"
    >
      <v-card-title class="text-white">{{ title }}</v-card-title>
      <v-card-subtitle class="text-white">{{ subtitle }}</v-card-subtitle>
      <v-card-text class="text-white">
        <p class="text-body-1 mb-4">{{ description }}</p>
        <div class="d-flex align-center mb-2">
          <v-icon
              icon="mdi-label"
              color="white"
              class="mr-2"
          ></v-icon>
          <span class="text-body-2">{{ category }}</span>
        </div>
        <div class="d-flex align-center mb-2">
          <v-icon
              icon="mdi-clock"
              color="white"
              class="mr-2"
          ></v-icon>
          <span class="text-body-2">{{
              new Date(timeDate).toLocaleString('en-US', {
                dateStyle: 'medium',
                timeStyle: 'short',
              })
            }}</span>
        </div>
        <div class="d-flex align-center">
          <v-icon
              icon="mdi-map-marker"
              color="white"
              class="mr-2"
          ></v-icon>
          <span class="text-body-2">{{ location }}</span>
        </div>
      </v-card-text>
    </v-img>
  </v-card>
</template>

<script>
export default {
  name: 'PostCard',
  props: {
    id: {
      type: [String, Number],
      required: true,
    },
    title: {
      type: String,
      required: true,
    },
    subtitle: {
      type: String,
      required: true,
    },
    description: {
      type: String,
      required: true,
    },
    category: {
      type: String,
      required: true,
    },
    timeDate: {
      type: String,
      required: true,
    },
    location: {
      type: String,
      required: true,
    },
    image: {
      type: String,
      required: true,
    },
  },
};
</script>

<style scoped>
.background-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}
.background-image {
  height: 100%;
  min-height: 300px;
}
.text-white {
  color: white;
}
.text-body-1 {
  line-height: 1.5;
}
.text-body-2 {
  color: rgba(255, 255, 255, 0.8);
}
.v-card-text {
  flex-grow: 1;
}
.v-card-title {
  background: rgba(0, 0, 0, 0.5);
  padding: 12px 16px;
  font-size: 1.5rem;
}
.v-card-subtitle {
  background: rgba(0, 0, 0, 0.5);
  padding: 8px 16px;
}
</style>
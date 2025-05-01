<template>
  <!-- Create Post Button -->
  <v-btn color="primary" @click="showModal = true">Create New Post</v-btn>

  <!-- Modal Popup -->
  <v-dialog v-model="showModal" max-width="500px">
    <v-card>
      <v-card-title>Create New Post</v-card-title>

      <v-card-text>
        <v-form ref="postForm" @submit.prevent="submitPost">
          <v-text-field v-model="form.title" label="Title"  :rules="[requiredRule]"/>
          <v-text-field v-model="form.subtitle" label="Subtitle" :rules="[requiredRule]" />
          <v-textarea v-model="form.description" label="Description" :rules="[requiredRule]" />
          <v-text-field v-model="form.category" label="Category" :rules="[requiredRule]" />
          <v-text-field v-model="form.location" label="Location" :rules="[requiredRule]" />

          <!-- Date Picker -->
          <v-menu v-model="dateMenu" transition="scale-transition" offset-y>
            <template v-slot:activator="{ props }">
              <v-text-field v-bind="props" v-model="form.date" label="Date" :rules="[dateRule]" readonly />
            </template>
            <v-date-picker v-model="form.date" @update:model-value="dateMenu = false" />
          </v-menu>

          <!-- Time Picker -->
          <v-menu v-model="timeMenu" transition="scale-transition" offset-y>
            <template v-slot:activator="{ props }">
              <v-text-field v-bind="props" v-model="form.time" label="Time" :rules="[timeRule]" readonly />
            </template>
            <v-time-picker
              v-model="form.time"
              format="24hr"
              @update:model-value="timeMenu = false"
            />
          </v-menu>

          <v-file-input 
            v-model="form.image"
            label="Upload Image"
            accept="image/*"
            prepend-icon="mdi-camera"
          />

          <v-btn type="submit" color="success" class="mt-4">Submit</v-btn>
        </v-form>
      </v-card-text>
    </v-card>
  </v-dialog>

  <!-- Posts Section -->
  <div class="page-wrapper">
    <div class="posts-box">
      <h2 class="text-h5 mb-4">Posts</h2>

      <v-alert v-if="posts.length === 0" type="info" text class="mb-4">
        No posts yet!
      </v-alert>

      <v-row v-else justify="center" align="stretch" class="post-grid">
        <v-col
            v-for="post in posts"
            :key="post.id"
            cols="12"
            sm="6"
            md="4"
            lg="3"
        >
          <v-hover v-slot="{ isHovering, props }">
            <v-card
                class="mx-auto"
                max-width="344"
                v-bind="props"
            >
              <v-img
                  :src="getImageUrl(post)"
                  height="200"
                  cover
                  @error="retryImage(post)"
              />

              <v-card-text>
                <h2 class="text-h6 text-primary">{{ post.title }}</h2>
                {{ post.subtitle }}
              </v-card-text>

              <v-overlay
                  :model-value="isHovering"
                  class="align-center justify-center"
                  scrim="rgba(0, 0, 0, 0.5)"
                  contained
                  style="border-radius: 12px;"
              >
                <div class="d-flex gap-4">
                  <v-btn icon color="orange" @click.stop="handleEdit(post)">
                    <v-icon>mdi-pencil</v-icon>
                  </v-btn>
                  <v-btn icon color="red" @click.stop="handleDelete(post.id)">
                    <v-icon>mdi-delete</v-icon>
                  </v-btn>
                </div>
              </v-overlay>
            </v-card>
          </v-hover>
        </v-col>

      </v-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, getCurrentInstance, watch } from "vue";
import EventCard from "@/components/EventCard.vue";
import TokenService from "@/scripts/TokenService.js";
import tokenService from "@/scripts/TokenService.js"; // Adjust path if needed
import {getUsernameFromToken} from "@/utils/jwt.js";

const postForm = ref(null);
const requiredRule = value => !!value || 'This field is required';
const dateRule = value => !!value || 'Date is required';
const timeRule = value => !!value || 'Time is required';

const posts = ref([]);//Holds the list of Events after you fetch.
const showModal = ref(false);
const dateMenu = ref(false);
const timeMenu = ref(false);
const hoveredPost = ref(null);
const isEditing = ref(false);
const selectedPostId = ref(null);
const form = ref({
  title: "",
  subtitle: "",
  description: "",
  category: "",
  location: "",
  date: "",
  time: "",
  image: null, // holds File object
  imageUrl: "", // holds encoded string
});

const getImageUrl = (post) => {
  return `http://localhost:80${post.imageUrl}?t=${post.retryKey || ''}`;
};

const retryImage = (post) => {
  setTimeout(() => {
    post.retryKey = Date.now();
  }, 500); // retry after 0.5s
};


// Axios setup
const { appContext } = getCurrentInstance();
const axios = appContext.config.globalProperties.$http;

//Used this method to remount
const fetchPosts = async () => {
  const username = getUsernameFromToken();
  try {
    const response = await axios.get(`http://localhost/api/events/user/${username}`);
    posts.value = response.data.map(post => ({
  ...post,
  retryKey: null
}));

  } catch (err) {
    console.error("Error fetching posts:", err);
  }
};

// Fetch posts from the backend when the component is mounted
onMounted(fetchPosts);

// Handle form submission for creating a post
const submitPost = async () => {
  const username = getUsernameFromToken();
  const { valid } = await postForm.value.validate();
  if (!valid) return;
  if (!form.value.date || !form.value.time) return;

  const toIsoString = (date, time) => {
    const yyyyMMdd = new Date(date).toISOString().split("T")[0];
    return `${yyyyMMdd}T${time}`;
  };
  const dateTime = toIsoString(form.value.date, form.value.time);

  const formData = new FormData();
  formData.append("title", form.value.title);
  formData.append("subtitle", form.value.subtitle);
  formData.append("description", form.value.description);
  formData.append("category", form.value.category);
  formData.append("location", form.value.location);
  formData.append("time", dateTime);
  formData.append("username", username);
 //This statement is for the editing post
  if(form.value.image){
    formData.append("image", form.value.image);
  }

  try {

    if(isEditing.value && selectedPostId.value){
      const response = await axios.put(`http://localhost/api/events/update/${selectedPostId.value}`, formData,);

      }else {
      const response = await axios.post("http://localhost/api/events/upload", formData,);
    }
    //Reset states and refresh
    showModal.value = false;
    isEditing.value = false;
    selectedPostId.value = null;
    setTimeout(async () => {
    await fetchPosts();
}, 500); // give backend time to save the image

  } catch (error) {
    console.error("Error creating post:", error);
    alert("Post was not created");
  }
};

const handleDelete = async (postId) => {
  try{
    await axios.delete(`http://localhost/api/events/delete/${postId}`);
    await fetchPosts();//Remounts the page
  } catch (error) {
    console.error("Error deleting post:", error);
    alert("Failed to delete post.");
  }
};

const handleEdit = (post) => {
  // 1) toggle into edit mode
  isEditing.value = true
  selectedPostId.value = post.id

  // 2) pre-fill the form
  form.value.title       = post.title
  form.value.subtitle    = post.subtitle
  form.value.description = post.description
  form.value.category    = post.category
  form.value.location    = post.location

  // split ISO timestamp into date + time
  const [date, time] = post.time.split("T")
  form.value.date = date
  form.value.time = time.slice(0,5)

  // keep existing image
  form.value.image    = null
  form.value.imageUrl = post.imageUrl

  // 3) open the modal
  showModal.value = true
};
/*
const onImageSelected = (files) => {
  const file = Array.isArray(files) ? files[0] : files;
  if (!file) return;

  const reader = new FileReader();
  reader.onload = () => {
    form.value.imageBase64 = reader.result;
    console.log("✅ Image encoded:", form.value.imageBase64.slice(0, 50), "..."); // preview
  };
  reader.readAsDataURL(file);
};
*/

/*
watch(() => form.value.image, (file) => {
  if (!file) return;

  const reader = new FileReader();
  reader.onload = () => {
    form.value.imageUrl = reader.result;
    console.log("✅ Encoded image:", reader.result.slice(0, 50), "...");
  };
  reader.readAsDataURL(file);
});
*/
</script>

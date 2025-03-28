<template>

  <v-sheet class="mx-auto" max-width="1000">
    <v-slide-group>
      <v-slide-item v-for="event in events" :key="event.id">
        <div class="card-wrapper">
          <EventCard 
            :id="event.id"
            :imageSrc="event.imageSrc" 
            :title="event.title" 
            :subtitle="event.subtitle"
            :content="event.content" 
          />
        </div>
      </v-slide-item>
    </v-slide-group>
  </v-sheet>


</template>

<script setup>
import EventCard from '@/components/EventCard.vue'
import { ref } from 'vue'
import event1Image from '@/assets/events/event1/image.jpg'
import event2Image from '@/assets/events/event2/image.jpg'
import event3Image from '@/assets/events/event3/image.jpg'
import event4Image from '@/assets/events/event4/image.jpg'
const show = ref(false)

const events = [
  {
    id: 1,
    imageSrc: event1Image,
    title: 'Outdoor Basketball Showdown',
    subtitle: 'Feel the energy of the court',
    content: "Lace up your sneakers, gather your friends, and get ready for an epic day on the court. Whether you’re a player or a passionate fan, this high-energy showdown will keep you on your toes from the opening tip-off to the final buzzer. Enjoy skillful plays, crowd roars, and a vibrant community atmosphere under the open sky."
  },
  {
    id: 2,
    imageSrc: event2Image,
    title: 'Graduation Commencement',
    subtitle: 'Celebrating Academic Achievements',
    content: "Join us for a memorable day of recognition and celebration as we honor the hard work and dedication of our graduating class. Witness proud graduates receive their diplomas, share in heartfelt speeches, and embrace the excitement of a new chapter. Gather with family, friends, and faculty to mark this meaningful milestone together."
  },
  {
    id: 3,
    imageSrc: event3Image,
    title: 'Evening of Inspiration',
    subtitle: 'Ideas that spark change',
    content: "Join a dynamic lineup of speakers, performers, and thought leaders for an immersive evening of inspiration. Connect with fellow attendees as you explore fresh perspectives, discover new opportunities, and share in the excitement of live presentations. Come prepared to learn, engage, and leave energized for what’s next."
  },
  {
    id: 4,
    imageSrc: event4Image,
    title: 'Advanced Mathematics Workshop',
    subtitle: 'Sharpen your problem-solving skills',
    content: "Dive into an interactive session exploring higher-level math concepts and practical problem-solving techniques. Experienced instructors will guide you through complex theorems, fostering a deeper understanding of advanced calculus and algebraic structures. Whether you’re an aspiring mathematician or a curious learner, this workshop offers the perfect opportunity to challenge yourself and collaborate with fellow enthusiasts."
  }
  // Add more event objects as needed
]
</script>

<style scoped>
.home
{
  padding: 20px;
}

/* This wrapper forces each card to a fixed width and adds spacing between them */
.card-wrapper
{
  width: 300px;
  /* Set the desired fixed width for each card */
  margin-right: 16px;
  /* Space between cards */
  padding: 10px;
}
</style>

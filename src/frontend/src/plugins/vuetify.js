import '@mdi/font/css/materialdesignicons.css' // Ensure you are using css-loader
import { createVuetify } from 'vuetify'
import { VTimePicker } from 'vuetify/labs/VTimePicker'

export default createVuetify({
  components: {
    VTimePicker,
  },
  icons: {
    defaultSet: 'mdi', // This is already the default value - only for display purposes
  },
})

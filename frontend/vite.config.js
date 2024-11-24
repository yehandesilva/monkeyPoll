import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  build: {
    outDir: '../backend/src/main/resources/static',
  },
  server: {
    proxy: {

      '/**': {
        target: 'https://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
    }
  }
})

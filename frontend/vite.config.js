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
      "/register": {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
      },
      "/login": {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
      },
      "/logout": {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
      },
      "/survey": {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false
      }
    }
  }
})

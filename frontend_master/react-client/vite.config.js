import {defineConfig} from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
    plugins: [react()],
    server: {
        proxy: {
            '/auth': {target: 'https://puggu.dev', changeOrigin: true, secure: true},
            '/api': {target: 'https://puggu.dev', changeOrigin: true, secure: true},
        },
        build: {
            outDir: '../../backend/src/main/resources/static/',
            emptyOutDir: false
        }
    }
})


import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import App from './App.tsx'
import './index.css'
import {store} from './app/store.ts'
import { Provider } from 'react-redux'
import { Toaster } from "@/components/ui/toaster"

document.documentElement.setAttribute('data-theme', "winter");

createRoot(document.getElementById('root')!).render(
  <StrictMode>
    <Provider store={store}>
      <App />
      <Toaster />
    </Provider>
  </StrictMode>,
)

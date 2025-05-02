import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import App from './App.tsx'
import {Provider} from "@/components/ui/provider"

createRoot(document.getElementById('root')!).render(
    <StrictMode>
        <Provider defaultTheme={"light"}>
            <App/>
        </Provider>
    </StrictMode>,
)

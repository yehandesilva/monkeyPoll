import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import App from './App.jsx'
import './index.css'
import {UserProvider} from "./context/UserContext.jsx";
import {SurveyProvider} from "./context/SurveyContext.jsx";

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <SurveyProvider>
            <UserProvider>
                <App/>
            </UserProvider>
        </SurveyProvider>

    </StrictMode>,
)

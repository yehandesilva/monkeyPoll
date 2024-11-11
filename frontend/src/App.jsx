import 'primereact/resources/themes/lara-dark-green/theme.css';
import "primeicons/primeicons.css";
import "primeflex/primeflex.css";

import {SurveyProvider} from "./context/SurveyContext.jsx";
import {UserProvider} from "./context/UserContext.jsx";
import Welcome from "./welcome/Welcome.jsx";
import MenuBar from "./MenuBar.jsx";
import Home from "./home/Home.jsx";

const App = () => {

    return (
        <SurveyProvider>
            <UserProvider>
                <div className="h-screen w-full relative" style={{backgroundColor: '#014F5E'}}>
                    <MenuBar/>
                    <Welcome/>
                </div>
            </UserProvider>
        </SurveyProvider>
    )
}

export default App

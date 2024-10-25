import 'primereact/resources/themes/lara-dark-teal/theme.css';
import "primeicons/primeicons.css";
import "primeflex/primeflex.css";

import Welcome from "./welcome/Welcome.jsx";
import MenuBar from "./main/MenuBar.jsx";

const App = () => {

    return (
        <>
            <div className="h-screen w-full relative" style={{backgroundColor: '#014F5E'}}>
                <MenuBar/>
                <Welcome/>
            </div>
        </>
    )
}

export default App

import {Button} from 'primereact/button';

const MenuBar = () => {
    return (
        <Button label="Login" icon="pi pi-sign-in" size="small" className="absolute top-0 right-0 m-4"
                style={{boxShadow: "none"}}/>
    )
}

export default MenuBar;
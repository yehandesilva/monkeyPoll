import {Button} from "primereact/button";

const Surveys = ({setVisible}) => {
    return (
        <>
            <Button label="Back" icon="pi pi-arrow-circle-left" size="small" className="absolute top-0 left-0 m-4"
                    style={{boxShadow: "none"}} onClick={() => setVisible(false)}/>
        </>
    )
}

export default Surveys;

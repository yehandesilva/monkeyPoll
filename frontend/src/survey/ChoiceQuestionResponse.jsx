import {Card} from "primereact/card";
import {Chart} from 'primereact/chart';
import {useEffect, useState} from "react";

const ChoiceQuestionResponse = ({response, index}) => {

    const [chartData, setChartData] = useState({});
    const [chartOptions, setChartOptions] = useState({});

    useEffect(() => {
        const documentStyle = getComputedStyle(document.documentElement);
        const data = {
            labels: response.responses.map((res) => Object.keys(res)[0]),
            datasets: [
                {
                    data: response.responses.map((res) => Object.values(res)[0]),
                    backgroundColor: [
                        documentStyle.getPropertyValue('--blue-500'),
                        documentStyle.getPropertyValue('--yellow-500'),
                        documentStyle.getPropertyValue('--green-500')
                    ],
                    hoverBackgroundColor: [
                        documentStyle.getPropertyValue('--blue-400'),
                        documentStyle.getPropertyValue('--yellow-400'),
                        documentStyle.getPropertyValue('--green-400')
                    ]
                }
            ]
        }
        const options = {
            plugins: {
                legend: {
                    labels: {
                        usePointStyle: true
                    }
                }
            }
        };

        setChartData(data);
        setChartOptions(options);
    }, []);

    return (
        <>
            <div key={index} className="w-6 py-4">
                <Card title={"Question " + (index + 1)}>
                    <h3>{response.question}</h3>
                    <div className="flex justify-content-center">
                        <Chart type="pie" data={chartData} options={chartOptions} className="flex w-6"/>
                    </div>
                </Card>
            </div>
        </>
    )
}

export default ChoiceQuestionResponse;
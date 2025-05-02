import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { Home } from "./pages/Home";
import { OrderPage } from "./pages/OrderPage";

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/order/:orderId" element={<OrderPage />} />
            </Routes>
        </Router>
    );
}

export default App;
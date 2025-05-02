import { Container } from "@chakra-ui/react";
import { ProductList } from "../components/ProductList";
import { Cart } from "../components/Cart";
import {useRef} from "react";


export const Home = () => {
    const refreshCartRef = useRef<() => void>(() => {});
    return (
        <Container maxW="container.xl" py={6}>
            <ProductList onAddToCart={() => refreshCartRef.current?.()}/>
            <Cart refreshRef={refreshCartRef}/>
        </Container>
    );
};

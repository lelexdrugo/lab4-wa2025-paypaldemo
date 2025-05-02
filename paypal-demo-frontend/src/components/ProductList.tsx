import { useEffect, useState } from "react";
import { fetchProducts } from "../api/api";
import { SimpleGrid, Heading, Spinner, Center } from "@chakra-ui/react";
import { ProductCard } from "./ProductCard";
import { Product } from "@/api/types.ts";
type ProductListProps = {
    onAddToCart?: () => void;
};
export const ProductList = ({ onAddToCart }: ProductListProps) => {
    const [products, setProducts] = useState<Product[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchProducts().then((res) => {
            setProducts(res.data);
            setLoading(false);
        });
    }, []);

    if (loading) {
        return (
            <Center py={10}>
                <Spinner size="xl" />
            </Center>
        );
    }

    return (
        <>
            <Heading my={6}>Product Catalog</Heading>
            <SimpleGrid columns={[1, 2, 3]} >
                {products.map((p) => (
                    <ProductCard key={p.id} {...p} onAddToCart={onAddToCart}/>
                ))}
            </SimpleGrid>
        </>
    );
};

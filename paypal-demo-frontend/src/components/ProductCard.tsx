import { Box, Button, Image, Text, VStack } from "@chakra-ui/react";
import { addToCart } from "../api/api";
import { Product } from "@/api/types.ts";

type ProductCardProps = Product & {
    onAddToCart?: () => void;
};;

export const ProductCard = ({ id, name, price,imageUrl ,onAddToCart}: ProductCardProps) => {
    const handleAddToCart = async () => {
        await addToCart(id);
        onAddToCart?.();
    };

    return (
        <Box  borderWidth="1px" borderRadius="lg" p={4}>
            <VStack>
                <Image src={imageUrl} boxSize="150px" objectFit="cover" />
                <Text fontWeight="bold">{name}</Text>
                <Text>${price.toFixed(2)}</Text>
                <Button onClick={handleAddToCart} colorPalette="teal">
                    Add to Cart
                </Button>
            </VStack>
        </Box>
    );
};

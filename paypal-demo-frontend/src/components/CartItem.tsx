import { HStack, Text } from "@chakra-ui/react";
import { CartItem as CartItemType } from "@/api/types.ts";

type CartItemProps = CartItemType;

export const CartItem = ({ quantity, product }: CartItemProps) => {
    return (
        <HStack justify="space-between" py={2}>
            <Text width={100}>{product.name}</Text>
            <Text>Qty: {quantity}</Text>
            <Text>${(product.price * quantity).toFixed(2)}</Text>
        </HStack>
    );
};

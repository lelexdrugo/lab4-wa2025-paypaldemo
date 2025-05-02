
export interface Product {
    id: number;
    name: string;
    price: number;
    imageUrl: string;
}

export interface CartItem {
    id: number;
    quantity: number;
    product: Product;
}

export interface Cart {
    id: number;
    status: CartStatus;
    items: CartItem[];
}

export enum CartStatus {
    CANCELLED,
    IN_PROGRESS,
    PAID,
    TO_FILL
}